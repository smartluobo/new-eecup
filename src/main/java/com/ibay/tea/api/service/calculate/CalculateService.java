package com.ibay.tea.api.service.calculate;

import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class CalculateService {


    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbCartMapper tbCartMapper;

    @Resource
    private StoreCache storeCache;

    @Resource
    private ApiCartService apiCartService;

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private TbFavorableCompanyMapper tbFavorableCompanyMapper;

    public CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo, boolean isCreateOrder) {

        TbUserCoupons tbUserCoupons = null;
        TbStore store = storeCache.findStoreById(cartOrderParamVo.getStoreId());
        int sendPrice = 0;
        if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = store.getSendCost();
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

            List<TbItem> goodsList = new ArrayList<>();
            for (TbCart cartItem : cartItemByIds) {
                totalGoodsCount += cartItem.getItemCount();
                TbItem tbItem = apiCartService.buildCartGoodsInfo(cartItem,store);
                if (tbItem != null){
                    if (maxPriceValue < tbItem.getCartPrice()) {
                        maxPriceValue = tbItem.getCartPrice();
                    }
                    log.info("");
                    orderTotalPrice += tbItem.getCartTotalPrice();
                    if (tbItem.getIsIngredients() == 0){
                        realGoodsCount += cartItem.getItemCount();
                    }
                    goodsList.add(tbItem);
                }
                log.info("goods info :title:{},price:{},cartPrice:{},cartTotalPrice:{},itemCount:{},skuDesc:{}",tbItem.getTitle(),tbItem.getPrice(),tbItem.getCartPrice(),tbItem.getCartTotalPrice(),tbItem.getCartItemCount(),tbItem.getSkuDetailDesc());
            }

            //特价活动计算
            TbActivity teJiaActivity = tbActivityMapper.findTeJiaActivity(String.valueOf(store.getId()), DateUtil.getDateYyyyMMdd());
            if (teJiaActivity != null){
                String goodsIds = teJiaActivity.getGoodsIds();
                Set<String> goodsIdSet = new HashSet<>(Arrays.asList(StringUtils.split(goodsIds, ",")));
                boolean flag = true;
                for (TbItem tbItem : goodsList) {
                    if (!goodsIdSet.contains(String.valueOf(tbItem.getId()))){
                        flag = false;
                        break;
                    }
                }
                if (flag){
                    CalculateReturnVo calculateReturnVo = tejiaCalculate(sendPrice, goodsList);
                    return calculateReturnVo;
                }
            }
            //全场折扣下不支持其他任何折扣
            TbActivity fullActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd(), store.getId());
            if (fullActivity != null){
                CalculateReturnVo calculateReturnVo = fullRatioCouponsCalculate(sendPrice, orderTotalPrice, goodsList, fullActivity);

                if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND ){
                    calculateReturnVo.setPostFee(store.getSendCost());
                }
                return calculateReturnVo;
            }
            String groupGiveName = null;
            String fullReduceName = null;
            String couponsName = null;
            //查看是否有买一送一 第二杯半价活动
            TbActivity tbActivity = tbActivityMapper.findSpecialActivity(store.getId().toString(), DateUtil.getDateYyyyMMdd());
            if (tbActivity != null){
                if (ApiConstant.ACTIVITY_TYPE_1_1 == tbActivity.getActivityType()){
                    specialReduceAmount = buyOneGiveOneReduceAmount(goodsList);
                    log.info("buyOneGiveOneReduceAmount specialReduceAmount : {}",specialReduceAmount);
                }else if (ApiConstant.ACTIVITY_TYPE_TWO_HALF == tbActivity.getActivityType()){
                    specialReduceAmount = twoHalfReduceAmount(goodsList);
                    log.info("twoHalfReduceAmount specialReduceAmount : {}",specialReduceAmount);
                }
            }
            if (realGoodsCount >= 6){
                //走满五赠一的流程，数量6 赠送一杯付款五杯价钱 数量12 赠送两杯 付款十杯价钱
                Map<String, Object> resultMap = groupReduceAmount(goodsList, realGoodsCount);
                if (resultMap.get("groupGiveName") != null){
                    groupGiveName = resultMap.get("groupGiveName").toString();
                }
                if (resultMap.get("groupGiveAmount") != null){
                    groupGiveAmount = Double.valueOf(resultMap.get("groupGiveAmount").toString());
                }
                log.info("groupReduceAmount -----groupGiveName:{},groupGiveAmount : {}",groupGiveName,groupGiveAmount);
            }
            //如果总价大于100
            if (orderTotalPrice >= 100){
                Map<String, Object> resultMap = fullReduceCalculate(orderTotalPrice);
                if (resultMap.get("fullReduceName") != null){
                    fullReduceName = resultMap.get("fullReduceName").toString();
                }
                if (resultMap.get("fullReduceAmount") != null){
                    fullReduceAmount = Double.valueOf(resultMap.get("fullReduceAmount").toString());
                }
                log.info("fullReduceCalculate -----fullReduceName:{},fullReduceAmount : {}",fullReduceName,fullReduceAmount);
            }

            if (tbUserCoupons != null) {
                Map<String, Object> resultMap = couponsReduceCalculate(maxPriceValue, orderTotalPrice, tbUserCoupons);

                if (resultMap.get("couponsName") != null){
                    couponsName = resultMap.get("couponsName").toString();
                }
                if (resultMap.get("couponsReduceAmount") != null){
                    couponsReduceAmount = Double.valueOf(resultMap.get("couponsReduceAmount").toString());
                }
                log.info("fullReduceCalculate -----couponsName:{},couponsReduceAmount : {}",couponsName,couponsReduceAmount);
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
            if ( specialReduceAmount > 0){

                calculateReturnVo.setOrderReduceAmount(specialReduceAmount);
                calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.subtract(calculateReturnVo.getOrderTotalAmount(),specialReduceAmount));
                calculateReturnVo.setGoodsList(goodsList);
                if (ApiConstant.ACTIVITY_TYPE_1_1 == tbActivity.getActivityType()){
                    calculateReturnVo.setCouponsName("买一赠一");
                    calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_1_1);
                }else {
                    calculateReturnVo.setCouponsName("第二杯半价");
                    calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_TWO_HALF);
                    log.info("use special activity  ");
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
            if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
                calculateReturnVo.setPostFee(store.getSendCost());
            }
            return calculateReturnVo;
        }
        return null;
    }

    private Map<String,Object> couponsReduceCalculate(double maxPriceValue, double orderTotalPrice, TbUserCoupons tbUserCoupons) {
        double couponsReduceAmount = 0.0;
        Map<String,Object> resultMap = new HashMap<>();

        if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_RATIO || tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_GENERAL){
            //打折优惠券,选择商品中单价最大的进行打折
            couponsReduceAmount = PriceCalculateUtil.ratioCouponsPriceCalculate(tbUserCoupons, maxPriceValue);
        }
        if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_FREE){
            //免费券 免费商品中单价最高的商品
            couponsReduceAmount = maxPriceValue;
        }
        if (tbUserCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_CASH){
            String cashAmount = tbUserCoupons.getCashAmount();
            if (cashAmount == null || Double.valueOf(cashAmount) == 0){
                couponsReduceAmount = 0;
            }else{
                Double cashAmountDouble = Double.valueOf(cashAmount);
                if (cashAmountDouble >= orderTotalPrice){
                    couponsReduceAmount = orderTotalPrice;
                }else {
                    couponsReduceAmount = cashAmountDouble;
                }
            }
        }
        resultMap.put("couponsReduceAmount",couponsReduceAmount);
        resultMap.put("couponsName",tbUserCoupons.getCouponsName());
        return resultMap;
    }

    private Map<String,Object> fullReduceCalculate(double orderTotalPrice) {
        Map<String,Object> resultMap = new HashMap<>();
        List<TbCoupons> tbCouponsList = tbCouponsMapper.findFullReduceCoupons();
        for (TbCoupons tbCoupons : tbCouponsList) {
            log.info("goods full reduce calculate tbCoupons :{}",tbCoupons);
            if (orderTotalPrice >= tbCoupons.getConsumeAmount()){
                double fullReduceAmount = tbCoupons.getReduceAmount();
                String fullReduceName = tbCoupons.getCouponsName();
                resultMap.put("fullReduceAmount",fullReduceAmount);
                resultMap.put("fullReduceName",fullReduceName);
                return resultMap;
            }
        }
        return resultMap;
    }

    private Map<String,Object> groupReduceAmount(List<TbItem> goodsList, int realGoodsCount) {
        List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
        Map<String,Object> resultMap = new HashMap<>();
        int giveCount = realGoodsCount / 6;
        String groupGiveName = "满"+(giveCount*5)+"杯送"+giveCount+"杯";
        Collections.sort(newGoodsList);
        double groupGiveAmount = 0.0;

        for (int i = 0; i< newGoodsList.size() && giveCount > 0; i++){
            TbItem tbItem = newGoodsList.get(i);
            if (tbItem.getCartItemCount() >= giveCount){
                log.info("");
                groupGiveAmount += PriceCalculateUtil.multiply(tbItem.getCartPrice(),String.valueOf(giveCount));
                break;
            }else if (giveCount > tbItem.getCartItemCount()){
                groupGiveAmount  += PriceCalculateUtil.multiply(tbItem.getCartPrice(), String.valueOf(tbItem.getCartItemCount()));
                giveCount -= tbItem.getCartItemCount();
            }else {
                log.info("");
                groupGiveAmount  += PriceCalculateUtil.multiply(tbItem.getCartPrice(), String.valueOf(giveCount));
                giveCount = 0;
            }
        }
        resultMap.put("groupGiveName",groupGiveName);
        resultMap.put("groupGiveAmount",groupGiveAmount);
        return resultMap;
    }

    private double buyOneGiveOneReduceAmount(List<TbItem> goodsList) {
        if (!CollectionUtils.isEmpty(goodsList)){
            List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
            List<TbItem> goodsUnitList = spiltGoods(newGoodsList);
            double specialReduceAmount = 0.0;
            Collections.sort(goodsUnitList);
            int goodsCount = goodsUnitList.size();
            if (goodsCount > 1){
                int giveCount = goodsCount / 2;
                for (int i = 0; i < giveCount; i++) {
                    specialReduceAmount = PriceCalculateUtil.add(specialReduceAmount,goodsUnitList.get(i).getCartPrice());
                }
            }
            return specialReduceAmount;
        }
        return 0.0;
    }

    private double twoHalfReduceAmount(List<TbItem> goodsList) {
        if (!CollectionUtils.isEmpty(goodsList)){
            List<TbItem> newGoodsList = removeSpecialGoods(goodsList);
            List<TbItem> goodsUnitList = spiltGoods(newGoodsList);
            double specialReduceAmount = 0.0;
            Collections.sort(goodsUnitList);
            int goodsCount = goodsUnitList.size();
            if (goodsCount > 1){
                int halfCount = goodsCount / 2;
                log.info("halfCount : {}",halfCount);
                for (int i = 0; i < halfCount; i++) {
                    double price = PriceCalculateUtil.multiply(goodsUnitList.get(i).getCartPrice(), "0.5");
                    specialReduceAmount = PriceCalculateUtil.add(specialReduceAmount,price);
                }
            }
            return specialReduceAmount;
        }
        return 0.0;

    }

    private List<TbItem> removeSpecialGoods(List<TbItem> goodsList) {
        List<TbItem> newGoodsList = new ArrayList<>();
        newGoodsList.addAll(goodsList);
        newGoodsList.removeIf(tbItem -> tbItem.getIsIngredients() == 1);
        return newGoodsList;
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

    private void buildReturnVo(CalculateReturnVo calculateReturnVo, double payAmount, double reduceAmount, String couponsName) {
        calculateReturnVo.setOrderPayAmount(payAmount);
        calculateReturnVo.setOrderReduceAmount(reduceAmount);
        calculateReturnVo.setCouponsName(couponsName);
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

    private CalculateReturnVo fullRatioCouponsCalculate(int sendPrice, double orderTotalPrice, List<TbItem> goodsList, TbActivity fullActivity) {
        log.info("进入全场折扣活动的价格计算");
        //全场折扣下所有商品不在重新计算优惠
        CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
        calculateReturnVo.setCouponsName("全场折扣下，不使用其他优惠");
        double payment = PriceCalculateUtil.multiply(orderTotalPrice, fullActivity.getActivityRatio());
        calculateReturnVo.setOrderReduceAmount(PriceCalculateUtil.subtract(orderTotalPrice,payment));
        orderTotalPrice += sendPrice;
        calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
        calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.add(payment,sendPrice));
        calculateReturnVo.setGoodsList(goodsList);
        calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_ALL_RATIO);
        return calculateReturnVo;
    }

    private CalculateReturnVo tejiaCalculate(int sendPrice, List<TbItem> goodsList) {
        log.info("特价活动的价格计算");
        //全场折扣下所有商品不在重新计算优惠
        double orderTotalPrice = 0.0;
        double payment = 0.0;
        for (TbItem tbItem : goodsList) {
            double itemPrice = PriceCalculateUtil.multiply(tbItem.getPrice(), tbItem.getCartItemCount());
            double itemTejiaPrice = PriceCalculateUtil.multiply(tbItem.getTejiaPrice(), tbItem.getCartItemCount());
            orderTotalPrice = PriceCalculateUtil.add(orderTotalPrice,itemPrice);
            payment = PriceCalculateUtil.add(payment,itemTejiaPrice);
        }
        CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
        calculateReturnVo.setCouponsName("特价商品，不使用其他优惠");
        calculateReturnVo.setOrderReduceAmount(PriceCalculateUtil.subtract(orderTotalPrice,payment));
        calculateReturnVo.setOrderTotalAmount(PriceCalculateUtil.add(orderTotalPrice,sendPrice));
        calculateReturnVo.setOrderPayAmount(PriceCalculateUtil.add(payment,sendPrice));
        calculateReturnVo.setGoodsList(goodsList);
        calculateReturnVo.setCouponsType(ApiConstant.COUPONS_STRATEGY_TYPE_TEJIA);
        return calculateReturnVo;
    }
}
