package com.ibay.tea.api.service.order;

import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;
import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.api.service.calculate.CalculateService;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.api.service.pay.ApiPayService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.common.utils.SerialGenerator;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class ApiOrderService {


    @Resource
    private TbCartMapper tbCartMapper;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbUserPayRecordMapper tbUserPayRecordMapper;

    @Resource
    private ApiCartService apiCartService;

    @Resource
    private ApiAddressService apiAddressService;

    @Resource
    private ActivityCache activityCache;

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private ApiPayService apiPayService;

    @Resource
    private StoreCache storeCache;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private ApiGoodsService apiGoodsService;

    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private PrintService printService;

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbFavorableCompanyMapper tbFavorableCompanyMapper;

    @Resource
    private CalculateService calculateService;

    @Resource
    private TbNoPaymentUserMapper tbNoPaymentUserMapper;

    public Map<String, Object> createOrderByCart(CartOrderParamVo cartOrderParamVo) throws Exception{

        int selfGet = cartOrderParamVo.getSelfGet();
        int addressId = cartOrderParamVo.getAddressId();
        String oppenId = cartOrderParamVo.getOppenId();
        int userCouponsId = cartOrderParamVo.getUserCouponsId();
        String cartItemIds = cartOrderParamVo.getCartItemIds();
        if (StringUtils.isEmpty(cartItemIds)){
            return null;
        }
        String[] cartItemIdArr = cartItemIds.split(",");
        int storeId = cartOrderParamVo.getStoreId();
        TbStore store = storeCache.findStoreById(storeId);

        //创建订单成功自动调起支付接口支付
        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return null;
        }
        CalculateReturnVo calculateReturnVo = calculateService.calculateCartOrderPrice(cartOrderParamVo,true);
        log.info("create order calculate order price  success......calculateReturnVo ; {}",calculateReturnVo);
        if (calculateReturnVo == null){
            return null;
        }
        List<TbItem> goodsList = calculateReturnVo.getGoodsList();
        if (CollectionUtils.isEmpty(goodsList)){
            return null;
        }
        List<TbOrderItem> tbOrderItems = new ArrayList<>();
        String orderId = SerialGenerator.getOrderSerial(store);
        int totalGoodsCount = 0;
        log.info("start for goods .....");
        for (TbItem tbItem : goodsList) {
            totalGoodsCount += tbItem.getCartItemCount();
            //创建订单item
            TbOrderItem tbOrderItem = buildOrderItem(tbItem,orderId);
            tbOrderItem.setPrice(tbItem.getCartPrice());
            tbOrderItem.setTotalFee(tbItem.getCartTotalPrice());
            tbOrderItem.setNum(tbItem.getCartItemCount());
            tbOrderItem.setSkuDetailIds(tbItem.getCartSkuDetailIds());
            tbOrderItem.setSkuDetailDesc(tbItem.getSkuDetailDesc());
            tbOrderItems.add(tbOrderItem);
        }

        TbOrder tbOrder = buildTbOrder(oppenId, selfGet, userAddress);
        tbOrder.setPosterUrl(goodsList.get(0).getImage());
        tbOrder.setStoreId(store.getId());
        tbOrder.setStoreName(store.getStoreName());
        tbOrder.setGoodsName(goodsList.get(0).getTitle());
        tbOrder.setGoodsTotalCount(totalGoodsCount);
        tbOrder.setBuyerMessage(cartOrderParamVo.getBuyerMessage());
        tbOrder.setUserCouponsName(calculateReturnVo.getCouponsName());
        if (calculateReturnVo.getCouponsType() == 3){
            tbOrder.setUserCouponsId(calculateReturnVo.getUserCouponsId());
            tbOrder.setUserCouponsName(calculateReturnVo.getUserCouponsName());
        }
        Integer historyOrderCount = tbOrderMapper.findHistoryOrderCount(oppenId);
        if (historyOrderCount == null || historyOrderCount == 0){
            tbOrder.setIsFirstOrder(1);
        }
        //订单实际金额
        tbOrder.setOrderPayment(calculateReturnVo.getOrderTotalAmount());
        //用户支付金额
        tbOrder.setPayment(calculateReturnVo.getOrderPayAmount());
        tbOrder.setCouponsReduceAmount(calculateReturnVo.getOrderReduceAmount());
        tbOrder.setOrderId(orderId);
        String takeCode = orderId.substring(orderId.length() - 4);
        tbOrder.setTakeCode(takeCode);
        //保存订单商品item
        if (tbOrderItems.size() > 1){
            tbOrderItemMapper.insertBatch(tbOrderItems);
        }else {
            tbOrderItemMapper.insert(tbOrderItems.get(0));
        }
        log.error("create order success.......");

        //如果订单有使用优惠券，更新优惠券状态
        TbUserCoupons tbUserCoupons = tbUserCouponsMapper.selectByPrimaryKey(userCouponsId);
        if (tbUserCoupons != null ){
            updateUserCoupons(tbOrder,tbUserCoupons,calculateReturnVo);
        }

        //如果减少金额等于订单金额 更新优惠券为已经使用 订单状态为已支付
        TbUserPayRecord tbUserPayRecord = buildPayRecordAndSave(oppenId, userCouponsId, orderId, tbOrder,calculateReturnVo.getCouponsType());

        //保存订单
        TbApiUser apiUserByOppenId = tbApiUserMapper.findApiUserByOppenId(oppenId);
        tbOrder.setUserAddressId(addressId);
        tbOrder.setBuyerNick(apiUserByOppenId.getNickName());
        if (userAddress == null){
            tbOrder.setPhoneNum(apiUserByOppenId.getUserBindPhoneNum());
        }
        tbOrder.setStatus(ApiConstant.ORDER_STATUS_NO_PAY);
        if (tbOrder.getPayment() == 0){
            tbOrder.setStatus(ApiConstant.ORDER_STATUS_PAYED);
        }
        log.info("current order info : {}",tbOrder);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND){
            tbOrder.setPostFee(store.getSendCost());
        }
        List<TbNoPaymentUser> noPaymentUsers = tbNoPaymentUserMapper.findByStoreId(storeId);
        if (!CollectionUtils.isEmpty(noPaymentUsers)){
            for (TbNoPaymentUser noPaymentUser : noPaymentUsers) {
                if (noPaymentUser.getOppenId().equals(oppenId)){
                    log.info("current oppenId : {} create order no pay",oppenId);
                    Map<String, Object> payMap = new HashMap<>();
                    //代下单订单状态
                    tbOrder.setStatus(8);
                    tbOrderMapper.insert(tbOrder);
                    //调用打印机
                    printService.printOrder(tbOrder,store,ApiConstant.PRINT_TYPE_ORDER_ALL);
                    tbCartMapper.deleteCartItemByIds(Arrays.asList(cartItemIdArr));
                    return payMap;
                }
            }
        }
        tbOrderMapper.insert(tbOrder);
        //判断是否需要调起支付
        if (calculateReturnVo.getOrderPayAmount() <= 0){
            //调用打印机
            printService.printOrder(tbOrder,store,ApiConstant.PRINT_TYPE_ORDER_ALL);
            tbCartMapper.deleteCartItemByIds(Arrays.asList(cartItemIdArr));
            return new HashMap<>();
        }

        //微信支付统一下单
        Map<String, Object> payMap = apiPayService.createPayOrderToWechat(tbOrder);
        log.info("wechat pay create order return result : {}",payMap);
        tbUserPayRecord.setNonceStr(String.valueOf(payMap.get("nonce_str")));
        tbUserPayRecord.setPrepayId(String.valueOf(payMap.get("prepay_id")));
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        payMap.put("timeStamp",timeStamp);
        payMap.put("signType",wechatInfoProperties.getSignType());
        String paySign = apiPayService.secondEncrypt(tbUserPayRecord,timeStamp);
        payMap.put("paySign",paySign);
        tbCartMapper.deleteCartItemByIds(Arrays.asList(cartItemIdArr));
        tbUserPayRecordMapper.insert(tbUserPayRecord);
        payMap.remove("mch_id");
        return payMap;
    }

    private TbOrderItem buildOrderItem(TbItem tbItem,String orderId) {
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setOrderId(orderId);
        tbOrderItem.setTitle(tbItem.getTitle());
        tbOrderItem.setPicPath(tbItem.getImage());
        return tbOrderItem;
    }

    private double getCouponsReduceAmount( double maxPriceValue, TbUserCoupons tbUserCoupons) {
        double couponsReduceAmount = 0.0;

        if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_RATIO || tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_GENERAL){
            //打折优惠券,选择商品中单价最大的进行打折
            couponsReduceAmount = PriceCalculateUtil.ratioCouponsPriceCalculate(tbUserCoupons, maxPriceValue);
        }
        if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_FREE){
            //免费券 免费商品中单价最高的商品
            couponsReduceAmount = maxPriceValue;
        }
        return couponsReduceAmount;
    }

    public Map<String, Object> createOrderByGoodsId(GoodsOrderParamVo goodsOrderParamVo) throws Exception{
        int selfGet = goodsOrderParamVo.getSelfGet();
        int addressId = goodsOrderParamVo.getAddressId();
        int userCouponsId = goodsOrderParamVo.getUserCouponsId();
        String oppenId = goodsOrderParamVo.getOppenId();
        String skuDetailIds = goodsOrderParamVo.getSkuDetailIds();
        int goodsCount = goodsOrderParamVo.getGoodsCount();
        int storeId = goodsOrderParamVo.getStoreId();
        TbStore store = storeCache.findStoreById(storeId);



        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return null;
        }

        CalculateReturnVo calculateReturnVo = calculateGoodsOrderPrice(goodsOrderParamVo, true);
        if (calculateReturnVo == null){
            return null;
        }
        TbItem goodsInfo = calculateReturnVo.getGoodsInfo();


        String orderId = SerialGenerator.getOrderSerial(store);

        //构建订单明细
        TbOrderItem tbOrderItem = buildOrderItem(goodsInfo,orderId);
        tbOrderItem.setPrice(goodsInfo.getPrice());
        if (goodsInfo.getShowActivityPrice() == 1){
            tbOrderItem.setPrice(goodsInfo.getActivityPrice());
        }

        tbOrderItem.setTotalFee(calculateReturnVo.getOrderTotalAmount());
        tbOrderItem.setNum(goodsCount);
        tbOrderItem.setSkuDetailIds(skuDetailIds);
        tbOrderItem.setSkuDetailDesc(goodsOrderParamVo.getSkuDetailDesc());
        TbOrder tbOrder = buildTbOrder(oppenId, selfGet, userAddress);
        tbOrder.setPosterUrl(goodsInfo.getImage());
        tbOrder.setGoodsName(goodsInfo.getTitle());
        tbOrder.setGoodsTotalCount(goodsCount);
        //订单实际金额
        tbOrder.setOrderPayment(calculateReturnVo.getOrderTotalAmount());
        //用户支付金额
        tbOrder.setPayment(calculateReturnVo.getOrderPayAmount());
        tbOrder.setOrderId(orderId);

        //保存订单商品item
        tbOrderItemMapper.insert(tbOrderItem);

        //如果订单有使用优惠券，更新优惠券状态
        TbUserCoupons tbUserCoupons = tbUserCouponsMapper.selectByPrimaryKey(userCouponsId);
        if (tbUserCoupons != null ){
            updateUserCoupons(tbOrder,tbUserCoupons,calculateReturnVo);
        }

        //如果减少金额等于订单金额 更新优惠券为已经使用 订单状态为已支付
        TbUserPayRecord tbUserPayRecord = buildPayRecordAndSave(oppenId, userCouponsId, orderId, tbOrder,calculateReturnVo.getCouponsType());
        tbUserPayRecordMapper.insert(tbUserPayRecord);
        //保存订单
        tbOrder.setStoreId(store.getId());
        tbOrder.setStoreName(store.getStoreName());
        tbOrder.setStatus(ApiConstant.ORDER_STATUS_NO_PAY);
        if (tbOrder.getPayment() == 0){
            tbOrder.setStatus(ApiConstant.ORDER_STATUS_PAYED);
        }
        tbOrderMapper.insert(tbOrder);
        //调用支付接口
        if (tbOrder.getStatus() == ApiConstant.ORDER_STATUS_NO_PAY){
            Map<String, Object> payMap = apiPayService.createPayOrderToWechat(tbOrder);
            tbUserPayRecord.setNonceStr(String.valueOf(payMap.get("nonce_str")));
            tbUserPayRecord.setPrepayId(String.valueOf(payMap.get("prepay_id")));
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            payMap.put("timeStamp",timeStamp);
            payMap.put("signType",wechatInfoProperties.getSignType());
            String paySign = apiPayService.secondEncrypt(tbUserPayRecord,timeStamp);
            payMap.put("paySign",paySign);
            tbUserPayRecordMapper.insert(tbUserPayRecord);
            payMap.remove("mch_id");
            return payMap;
        }else{
            return new HashMap<>();
        }

    }

    private void updateUserCoupons(TbOrder tbOrder, TbUserCoupons tbUserCoupons, CalculateReturnVo calculateReturnVo) {
        if (calculateReturnVo.getCouponsType() == 3){
            if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_CASH){
                if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_CASH){
                    String cashAmount = tbUserCoupons.getCashAmount();
                    double remainingAmount = PriceCalculateUtil.subtract(cashAmount, String.valueOf(tbOrder.getCouponsReduceAmount()));
                    tbUserCouponsMapper.updateCashAmount(tbUserCoupons.getId(),remainingAmount);
                }
            }else{
                tbUserCouponsMapper.updateStatusById(tbUserCoupons.getId(), ApiConstant.USER_COUPONS_STATUS_USED);
                tbOrder.setStatus(ApiConstant.ORDER_STATUS_PAYED);
            }
        }
    }


    private TbUserPayRecord buildPayRecordAndSave(String oppenId, int userCouponsId, String orderId, TbOrder tbOrder,int couponsType) {
        //生成付款记录
        TbUserPayRecord tbUserPayRecord = new TbUserPayRecord();
        String payId = SerialGenerator.getPayRecordSerial();
        tbUserPayRecord.setId(payId);
        tbUserPayRecord.setCreateTime(new Date());
        tbUserPayRecord.setOppenId(oppenId);
        tbUserPayRecord.setOrderId(orderId);
        tbUserPayRecord.setOrderPayment(tbOrder.getOrderPayment());
        tbUserPayRecord.setPayment(tbOrder.getPayment());
        return tbUserPayRecord;

    }

    private TbOrder buildTbOrder(String oppenId, int selfGet, TbApiUserAddress userAddress) {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setPaymentType(1);
        tbOrder.setStatus(0);
        tbOrder.setCreateTime(new Date());
        tbOrder.setOppenId(oppenId);
        tbOrder.setSelfGet(selfGet);
        tbOrder.setAddress(userAddress == null ? "自提":userAddress.getAddressName());
        tbOrder.setPhoneNum(userAddress == null ? "" : userAddress.getBindNum());
        return tbOrder;
    }

    public boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, int userCouponsId, int addressId, int selfGet) {
        return (selfGet != ApiConstant.ORDER_TAKE_WAY_SEND || addressId != 0) && goodsId != 0;
    }

    public boolean checkCartOrderParameter(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet) {
        TbApiUser apiUser = tbApiUserMapper.findApiUserByOppenId(oppenId);
        if (apiUser == null){
            return false;
        }
        if (cartItemIds == null || cartItemIds.trim().length() == 0){
            return false;
        }
        return selfGet != ApiConstant.ORDER_TAKE_WAY_SEND || addressId != 0;
    }

    public CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo,boolean isCreateOrder) {

        TbUserCoupons tbUserCoupons = null;
        int sendPrice = 0;
        if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
           //todo
        }
        int userCouponsId = cartOrderParamVo.getUserCouponsId();
        String oppenId = cartOrderParamVo.getOppenId();
        String cartItemIds = cartOrderParamVo.getCartItemIds();
        if (userCouponsId != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(oppenId,userCouponsId);
        }

        if (cartItemIds != null && cartItemIds.trim().length() > 0) {
            //订单总商品数
            int totalGoodsCount = 0;
            int realGoodsCount = 0;
            //订单总价格
            double orderTotalPrice = 0.0;
            //最大单价
            double maxPriceValue = 0.0;
            //优惠减少金额
            double couponsReduceAmount = 0.0;
            //全场折扣减少金额
            double fullReduceAmount = 0.0 ;
            //满五赠一减少金额
            double groupGiveAmount = 0.0;

            double specialReduceAmount = 0.0;

            double goodsTotalPrice = 0.0;

            String[] cartItemIdArr = cartItemIds.split(",");
            List<TbCart> cartItemByIds = tbCartMapper.findCartItemByIds(Arrays.asList(cartItemIdArr));
            TbStore store = storeCache.findStoreById(cartOrderParamVo.getStoreId());
            List<TbItem> goodsList = new ArrayList<>();
            for (TbCart cartItem : cartItemByIds) {
                totalGoodsCount += cartItem.getItemCount();
                TbItem tbItem = apiCartService.buildCartGoodsInfo(cartItem,store);
                if (tbItem != null){
                    if (maxPriceValue < tbItem.getCartPrice()) {
                        maxPriceValue = tbItem.getCartPrice();
                    }
                    orderTotalPrice += tbItem.getCartTotalPrice();
                    if (tbItem.getIsIngredients() == 0){
                        realGoodsCount += cartItem.getItemCount();
                    }
                    goodsList.add(tbItem);
                }
                log.info("info :title:{},price:{},cartPrice:{},cartTotalPrice:{},itemCount:{},skuDesc:{}",tbItem.getTitle(),tbItem.getPrice(),tbItem.getCartPrice(),tbItem.getCartTotalPrice(),tbItem.getCartItemCount(),tbItem.getSkuDetailDesc());
            }

            Integer historyOrderCount = tbOrderMapper.findHistoryOrderCount(oppenId);
            double newUserReduceAmount = 0.0;
            if (historyOrderCount == null || historyOrderCount == 0){
                //new user create order
                List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
                Collections.sort(newGoodsList);
                if (newGoodsList.size() > 0 ){
                    double cartPrice = newGoodsList.get(newGoodsList.size()-1).getCartPrice();
                    newUserReduceAmount = PriceCalculateUtil.multiply(cartPrice,"0.5");
                }

                CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
                if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
                    orderTotalPrice += sendPrice;
                }
                calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
                calculateReturnVo.setCouponsName("新用户首杯半价");
                calculateReturnVo.setCouponsType(4);
                calculateReturnVo.setOrderReduceAmount(newUserReduceAmount);
                calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.subtract(calculateReturnVo.getOrderTotalAmount(),newUserReduceAmount));
                calculateReturnVo.setGoodsList(goodsList);
                return calculateReturnVo;
            }

            TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(cartOrderParamVo.getStoreId());
            if (todayActivityBean != null && todayActivityBean.getTbActivity() != null){
                if (ApiConstant.ACTIVITY_TYPE_FULL == todayActivityBean.getTbActivity().getActivityType()){
                    return fullRatioCouponsCalculate(sendPrice, orderTotalPrice, goodsList, todayActivityBean);
                }
            }

            String groupGiveName = null;
            String fullReduceName = null;
            String couponsName = null;
            TbActivity tbActivity = tbActivityMapper.findSpecialActivity(store.getId().toString(),DateUtil.getDateYyyyMMdd());
            if (tbActivity != null){
                if (ApiConstant.ACTIVITY_TYPE_1_1 == tbActivity.getActivityType()){
                    if (!CollectionUtils.isEmpty(goodsList)){
                        List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
                        List<TbItem> goodsUnitList = spiltGoods(newGoodsList);
                        Collections.sort(goodsUnitList);
                        int goodsCount = goodsUnitList.size();
                        if (goodsCount > 1){
                            int giveCount = goodsCount / 2;
                            for (int i = 0; i < giveCount; i++) {
                                specialReduceAmount = PriceCalculateUtil.add(specialReduceAmount,goodsUnitList.get(i).getCartPrice());
                            }
                        }
                    }
                }else if (ApiConstant.ACTIVITY_TYPE_TWO_HALF == tbActivity.getActivityType()){
                    if (!CollectionUtils.isEmpty(goodsList)){
                        List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
                        List<TbItem> goodsUnitList = spiltGoods(newGoodsList);
                        Collections.sort(goodsUnitList);
                        int goodsCount = goodsUnitList.size();
                        if (goodsCount > 1){
                            int giveCount = goodsCount / 2;
                            for (int i = 0; i < giveCount; i++) {
                                double price = PriceCalculateUtil.multiply(goodsUnitList.get(i).getCartPrice(), "0.5");
                                specialReduceAmount = PriceCalculateUtil.add(specialReduceAmount,price);
                            }
                        }
                    }
                }
            }
            if (realGoodsCount >= 6){
                //走满五赠一的流程，数量6 赠送一杯付款五杯价钱 数量12 赠送两杯 付款十杯价钱
                List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
                int giveCount = realGoodsCount / 6;
                groupGiveName = "满"+(giveCount*5)+"杯送"+giveCount+"杯";
                Collections.sort(newGoodsList);

                for (int i = 0; i< newGoodsList.size() && giveCount > 0; i++){
                    TbItem tbItem = newGoodsList.get(i);
                    if (tbItem.getCartItemCount() >= giveCount){
                        groupGiveAmount += PriceCalculateUtil.multiply(tbItem.getCartPrice(),String.valueOf(giveCount));
                        break;
                    }else if (giveCount > tbItem.getCartItemCount()){
                        groupGiveAmount  += PriceCalculateUtil.multiply(tbItem.getCartPrice(), String.valueOf(tbItem.getCartItemCount()));
                        giveCount -= tbItem.getCartItemCount();
                    }else {
                        groupGiveAmount  += PriceCalculateUtil.multiply(tbItem.getCartPrice(), String.valueOf(giveCount));
                        giveCount = 0;
                    }
                }
            }
            if (orderTotalPrice >= 100){
                List<TbCoupons> tbCouponsList = tbCouponsMapper.findFullReduceCoupons();
                log.info("goods full reduce calculate");
                for (TbCoupons tbCoupons : tbCouponsList) {
                    if (orderTotalPrice >= tbCoupons.getConsumeAmount()){
                        fullReduceAmount = tbCoupons.getReduceAmount();
                        fullReduceName = tbCoupons.getCouponsName();
                        break;
                    }
                }
            }

            if (tbUserCoupons != null) {
                log.info("user select coupons  userCouponsInfo: {}",tbUserCoupons);
                couponsReduceAmount = getCouponsReduceAmount(maxPriceValue,tbUserCoupons);
                couponsName = tbUserCoupons.getCouponsName();
            }

            goodsTotalPrice = orderTotalPrice;
            //判断哪种策略对消费者最优惠
            if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
                orderTotalPrice += sendPrice;
            }
            CalculateReturnVo calculateReturnVo = getCalculateReturnVo(orderTotalPrice, couponsReduceAmount, couponsName, groupGiveAmount, groupGiveName, fullReduceAmount, fullReduceName);
            if (calculateReturnVo.getCouponsType() == ApiConstant.COUPONS_STRATEGY_TYPE_COUPONS){
                calculateReturnVo.setUserCouponsId(tbUserCoupons.getId());
                calculateReturnVo.setUserCouponsName(tbUserCoupons.getCouponsName());
            }

            if (isCreateOrder){
                calculateReturnVo.setGoodsList(goodsList);
            }
            if (specialReduceAmount >= calculateReturnVo.getOrderReduceAmount() && specialReduceAmount > 0){

                calculateReturnVo.setOrderReduceAmount(specialReduceAmount);
                calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.subtract(calculateReturnVo.getOrderTotalAmount(),specialReduceAmount));
                calculateReturnVo.setGoodsList(goodsList);
                if (ApiConstant.ACTIVITY_TYPE_1_1 == tbActivity.getActivityType()){
                    calculateReturnVo.setCouponsName("买一赠一");
                    calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_1_1);
                }else {
                    calculateReturnVo.setCouponsName("第二杯半价");
                    calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_TWO_HALF);
                }
            }
            //判断用户是否是vip公司用户
            TbApiUser apiUserByOppenId = tbApiUserMapper.findApiUserByOppenId(oppenId);
            if (apiUserByOppenId.getCompanyId() != 0){
                TbFavorableCompany tbFavorableCompany = tbFavorableCompanyMapper.selectByPrimaryKey(apiUserByOppenId.getCompanyId());
                if (tbFavorableCompany != null){
                    //vip公司用户优惠计算
                    double companyFavorablePrice = PriceCalculateUtil.multiply(goodsTotalPrice, tbFavorableCompany.getCompanyRatio());
                    double companyReduceAmount = PriceCalculateUtil.subtract(goodsTotalPrice,companyFavorablePrice);
                    log.info("goodsTotalPrice : {},tbFavorableCompany : {}",goodsTotalPrice,companyFavorablePrice);
                    if (companyReduceAmount > calculateReturnVo.getOrderReduceAmount()){
                        //公司优惠更加选择公司优惠
                        calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
                        calculateReturnVo.setOrderReduceAmount(companyReduceAmount);
                        calculateReturnVo.setUserCouponsName("优质企业员工福利");
                        calculateReturnVo.setCouponsName("优质企业员工福利");
                        calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_COMPANY);
                        calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.subtract(orderTotalPrice,companyReduceAmount));
                    }
                }
            }
            log.info("current order totalGoodsCount : {},realGoodsCount:{}",totalGoodsCount,realGoodsCount);
            return calculateReturnVo;
        }
        return null;
    }

    private List<TbItem> spiltGoods(List<TbItem> newGoodsList) {
        List<TbItem> goodsUnitList = new ArrayList<>();
        for (TbItem tbItem : newGoodsList) {
            if (tbItem.getCartItemCount() > 1){
                for (int i = 0; i < tbItem.getCartItemCount(); i++) {
                    goodsUnitList.add(tbItem);
                }
            }else {
                goodsUnitList.add(tbItem);
            }
        }
        return goodsUnitList;
    }

    private List<TbItem> removeSpecialGoods(List<TbItem> goodsList) {
        List<TbItem> newGoodsList = new ArrayList<>();
        newGoodsList.addAll(goodsList);
        newGoodsList.removeIf(tbItem -> tbItem.getIsIngredients() == 1);
        return newGoodsList;
    }

    private CalculateReturnVo fullRatioCouponsCalculate(int sendPrice, double orderTotalPrice, List<TbItem> goodsList, TodayActivityBean todayActivityBean) {
        log.info("进入全场折扣活动的价格计算");
        //全场折扣下所有商品不在重新计算优惠

        CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
        calculateReturnVo.setCouponsName("全场折扣下，不使用其他优惠");

        TbActivity tbActivity = todayActivityBean.getTbActivity();
        List<TbActivityCouponsRecord> activityCouponsRecordsByActivityId = activityCache.getActivityCouponsRecordsByActivityId(tbActivity.getId());
        TbActivityCouponsRecord couponsRecord = activityCouponsRecordsByActivityId.get(0);
        TbCoupons tbCouponsById = activityCache.getTbCouponsById(couponsRecord.getCouponsId());
        double payment = PriceCalculateUtil.multiply(orderTotalPrice, tbCouponsById.getCouponsRatio());
        log.info("full activity payment : {}",payment);

        calculateReturnVo.setOrderReduceAmount(PriceCalculateUtil.subtract(orderTotalPrice,payment));
        orderTotalPrice += sendPrice;
        calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
        calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.add(payment,sendPrice));
        calculateReturnVo.setGoodsList(goodsList);
        calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_ALL_RATIO);
        log.info("full activity calculateReturnVo :{}",calculateReturnVo);
        return calculateReturnVo;
    }

    private void buildReturnVo(CalculateReturnVo calculateReturnVo, double payAmount, double reduceAmount, String couponsName) {
        calculateReturnVo.setOrderPayAmount(payAmount);
        calculateReturnVo.setOrderReduceAmount(reduceAmount);
        calculateReturnVo.setCouponsName(couponsName);
    }

    public CalculateReturnVo calculateGoodsOrderPrice(GoodsOrderParamVo goodsOrderParamVo,boolean isCreateOrder){

        int sendPrice = 0;
        int skuPrice = 0;
        TbUserCoupons tbUserCoupons = null;
        if (goodsOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }

        if (goodsOrderParamVo.getUserCouponsId() != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(goodsOrderParamVo.getOppenId(),goodsOrderParamVo.getUserCouponsId());
        }

        TbItem goods = goodsCache.findGoodsById(goodsOrderParamVo.getGoodsId());
        if (goods == null){
            return null;
        }
        goods = goods.copy();

        TbStore store = storeCache.findStoreById(goodsOrderParamVo.getStoreId());
        TbActivity fullActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd(), store.getId());
        apiGoodsService.calculateGoodsPrice(goods,store.getExtraPrice(),fullActivity);

        double goodsPrice = goods.getPrice();
        if (goods.getShowActivityPrice() == 1){
            goodsPrice = goods.getActivityPrice();
        }

        if (goodsOrderParamVo.getSkuDetailIds() != null ){
            skuPrice = goodsCache.calculateSkuPrice(goodsOrderParamVo.getSkuDetailIds());
        }

        goodsPrice = goodsPrice + skuPrice;
        goods.setPrice(goods.getPrice() + skuPrice);
        goods.setActivityPrice(goods.getActivityPrice() + skuPrice);
        goods.setCartItemCount(goodsOrderParamVo.getGoodsCount());
        goods.setCartSkuDetailIds(goodsOrderParamVo.getSkuDetailIds());
        goods.setSkuDetailDesc(goodsOrderParamVo.getSkuDetailDesc());
        double orderTotalPrice = PriceCalculateUtil.multiply(goodsPrice,goodsOrderParamVo.getGoodsCount());
        double couponsReduceAmount = 0.0;
        String couponsName = null;
        double groupGiveAmount = 0.0;
        String groupGiveName = null;
        double fullReduceAmount = 0.0;
        String fullReduceName = null;
        int goodsCount = goodsOrderParamVo.getGoodsCount();

        if (goodsCount >=6){
            //走满五赠一的流程，数量6 赠送一杯付款五杯价钱 数量12 赠送两杯 付款十杯价钱
            int giveCount = goodsCount / 6;
            groupGiveName = "满"+(giveCount*5)+"杯送"+giveCount+"杯";
            groupGiveAmount += PriceCalculateUtil.multiply(goodsPrice,String.valueOf(giveCount));
        }
        if (orderTotalPrice >= 100){
            List<TbCoupons> tbCouponsList = tbCouponsMapper.findFullReduceCoupons();
            for (TbCoupons tbCoupons : tbCouponsList) {
                if (orderTotalPrice >= tbCoupons.getConsumeAmount()){
                    fullReduceAmount = tbCoupons.getReduceAmount();
                    fullReduceName = tbCoupons.getCouponsName();
                    break;
                }
            }

        }

        if (tbUserCoupons != null){
            couponsReduceAmount = getCouponsReduceAmount(goodsPrice,tbUserCoupons);
            couponsName = tbUserCoupons.getCouponsName();
        }
        //判断哪种策略对消费者最优惠
        if (goodsOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            orderTotalPrice += sendPrice;
        }

        CalculateReturnVo calculateReturnVo = getCalculateReturnVo(orderTotalPrice, couponsReduceAmount, couponsName, groupGiveAmount, groupGiveName, fullReduceAmount, fullReduceName);
        if (calculateReturnVo != null && isCreateOrder){
            calculateReturnVo.setGoodsInfo(goods);
        }
        return calculateReturnVo;
    }

    private CalculateReturnVo getCalculateReturnVo(double orderTotalPrice, double couponsReduceAmount, String couponsName, double groupGiveAmount, String groupGiveName, double fullReduceAmount, String fullReduceName) {
        double maxReduceAmount = Math.max(groupGiveAmount, Math.max(fullReduceAmount, couponsReduceAmount));
        CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
        calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
        calculateReturnVo.setOrderPayAmount(orderTotalPrice);
        calculateReturnVo.setCouponsName("无优惠");
        calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_NO);
        if (maxReduceAmount == 0){
            return calculateReturnVo;
        }else if (maxReduceAmount == groupGiveAmount){
            double payAmount = PriceCalculateUtil.subtract(orderTotalPrice, groupGiveAmount);
            buildReturnVo(calculateReturnVo,payAmount,groupGiveAmount,groupGiveName);
            calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_GROUP);

        }else if (maxReduceAmount == fullReduceAmount){
            double payAmount = PriceCalculateUtil.subtract(orderTotalPrice, fullReduceAmount);
            buildReturnVo(calculateReturnVo,payAmount,fullReduceAmount,fullReduceName);
            calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_FULL_REDUCE);

        }else if (maxReduceAmount == couponsReduceAmount){
            double payAmount = PriceCalculateUtil.subtract(orderTotalPrice, couponsReduceAmount);
            buildReturnVo(calculateReturnVo,payAmount,couponsReduceAmount,couponsName);
            calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_COUPONS);
        }
        return calculateReturnVo;
    }

    public TbOrder findOrderDetailById(String orderId) {
        return tbOrderMapper.selectByPrimaryKey(orderId);
    }

    public List<TbOrderItem> findOrderItemByOrderId(String orderId) {
        return tbOrderItemMapper.findOrderItemByOrderId(orderId);
    }

    public List<TbOrder> findOrderByOppenId(String oppenId) {
        List<TbOrder> orderByOppenId = tbOrderMapper.findOrderByOppenId(oppenId);
        if (!CollectionUtils.isEmpty(orderByOppenId)){
            for (TbOrder tbOrder : orderByOppenId) {
                tbOrder.setCreateDateStr(DateUtil.viewDateFormat(tbOrder.getCreateTime()));
            }
        }
        return orderByOppenId;
    }

    public void cancelOrder(String oppenId, String orderId) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        if (tbOrder == null ){
            return;
        }
        if (tbOrder.getStatus() != 0){
            return;
        }
        tbOrderMapper.cancelOrder(oppenId,orderId);
        if (tbOrder.getUserCouponsId() != 0){
            TbUserCoupons tbUserCoupons = tbUserCouponsMapper.selectByPrimaryKey(tbOrder.getUserCouponsId());
            if (tbUserCoupons != null){
                if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_CASH){
                    double remainingAmount = PriceCalculateUtil.add(String.valueOf(tbOrder.getCouponsReduceAmount()), tbUserCoupons.getCashAmount());
                    tbUserCouponsMapper.updateCashAmount(tbUserCoupons.getId(),remainingAmount);
                }else{
                    tbUserCouponsMapper.updateStatusById(tbOrder.getUserCouponsId(),0);
                }
            }
        }
    }
}
