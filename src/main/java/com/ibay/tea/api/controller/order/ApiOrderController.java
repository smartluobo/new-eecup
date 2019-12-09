package com.ibay.tea.api.controller.order;

import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;
import com.ibay.tea.api.service.calculate.CalculateService;
import com.ibay.tea.api.service.order.ApiOrderService;
import com.ibay.tea.api.service.pay.ApiPayService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.WechatXmlParser;
import com.ibay.tea.common.utils.WxUtil;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/order")
public class ApiOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderController.class);

    @Resource
    private ApiOrderService apiOrderService;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private ApiPayService apiPayService;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private CalculateService calculateService;

    @RequestMapping("createOrderByCart")
    public ResultInfo createOrderByCart(@RequestBody CartOrderParamVo cartOrderParamVo){
        if (cartOrderParamVo == null){
            LOGGER.error("createOrderByCart cartOrderParamVo is null");
            return ResultInfo.newEmptyParamsResultInfo();
        }
        //判断是否有订单id 如果有订单id 直接调用订单支付接口不用创建订单
        String orderId = cartOrderParamVo.getOrderId();
        if (StringUtils.isNotEmpty(orderId)){
            //直接走支付流程
            Map<String,Object> resultMap = apiPayService.payByOrderId(orderId);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData(resultMap);
            return resultInfo;
        }
        LOGGER.info("createOrderByCart cartOrderParamVo : {}",cartOrderParamVo);
        String oppenId = cartOrderParamVo.getOppenId();
        String cartItemIds = cartOrderParamVo.getCartItemIds();
        int userCouponsId = cartOrderParamVo.getUserCouponsId();
        int addressId = cartOrderParamVo.getAddressId();
        int selfGet = cartOrderParamVo.getSelfGet();
        TbStore tbStore = tbStoreMapper.selectByPrimaryKey(cartOrderParamVo.getStoreId());
        if (tbStore == null){
            LOGGER.error("createOrderByCart storeId : {} not found store record ",cartOrderParamVo.getStoreId());
            return ResultInfo.newEmptyParamsResultInfo();

        }
        try {
            LOGGER.info("createOrderByCart params oppenId : {}, cartItemIds : {}, userCouponsId: {} ,addressId :{} selfGet : {}",
                    oppenId,cartItemIds,userCouponsId,addressId,selfGet);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            boolean flag = apiOrderService.checkCartOrderParameter(oppenId,cartItemIds,userCouponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            Map<String, Object> payMap = apiOrderService.createOrderByCart(cartOrderParamVo);

            resultInfo.setData(payMap);
            return resultInfo;

        }catch (Exception e){
            LOGGER.error("createOrderByCart happen exception oppenId : {}, cartItemIds : {}, userCouponsId: {} ,addressId :{} selfGet : {}",
                    oppenId,cartItemIds,userCouponsId,addressId,selfGet,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/createOrderByGoodsId")
    public ResultInfo createOrderByGoodsId(@RequestBody GoodsOrderParamVo goodsOrderParamVo){
        if (goodsOrderParamVo == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        //判断是否有订单id 如果有订单id 直接调用订单支付接口不用创建订单
        String orderId = goodsOrderParamVo.getOrderId();
        if (StringUtils.isNotEmpty(orderId)){
            LOGGER.info("createOrderByGoodsId orderId is not null payByOrderId orderId : {}",orderId);
            //直接走支付流程
            Map<String,Object> resultMap = apiPayService.payByOrderId(orderId);

            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData(resultMap);
            return resultInfo;
        }
        String oppenId = goodsOrderParamVo.getOppenId();
        String skuDetailIds = goodsOrderParamVo.getSkuDetailIds();
        long goodsId = goodsOrderParamVo.getGoodsId();
        int userCouponsId = goodsOrderParamVo.getUserCouponsId();
        int addressId = goodsOrderParamVo.getAddressId();
        int selfGet = goodsOrderParamVo.getSelfGet();
        int goodsCount = goodsOrderParamVo.getGoodsCount();
        TbStore tbStore = tbStoreMapper.selectByPrimaryKey(goodsOrderParamVo.getStoreId());

        LOGGER.info("createOrderByGoodsId params oppenId : {}, goodsId : {},skuDetailIds : {} userCouponsId: {} ,addressId :{} selfGet : {} goodsCount : {}",
                oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet,goodsCount);
        if (tbStore == null){
            LOGGER.error("createOrderByGoodsId storeId : {} not found store record ",goodsOrderParamVo.getStoreId());
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            boolean flag = apiOrderService.checkGoodsOrderParameter(oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

            Map<String, Object> payMap = apiOrderService.createOrderByGoodsId(goodsOrderParamVo);
            resultInfo.setData(payMap);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("createOrderByGoodsId happen exception oppenId : {}, goodsId : {},skuDetailIds : {} userCouponsId: {} ,addressId :{} selfGet : {} goodsCount : {}",
                    oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet,goodsCount,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/findOrderDetailById")
    public ResultInfo findOrderDetailById(@RequestBody Map<String,String> params){
        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String orderId = params.get("orderId");
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbOrder orderInfo = apiOrderService.findOrderDetailById(orderId);
            orderInfo.setCreateDateStr(DateUtil.viewDateFormat(orderInfo.getCreateTime()));
            List<TbOrderItem> orderItemList = apiOrderService.findOrderItemByOrderId(orderId);
            orderInfo.setOrderItems(orderItemList);
            resultInfo.setData(orderInfo);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("findOrderDetailById happen exception orderId ; {}",orderId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/findOrderByOppenId")
    public ResultInfo findOrderByOppenId(@RequestBody Map<String,String> params){
        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbOrder> orderList = apiOrderService.findOrderByOppenId(oppenId);
            resultInfo.setData(orderList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("findOrderByOppenId happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/calculateCartOrderPrice")
    public ResultInfo calculateCartOrderPrice(@RequestBody CartOrderParamVo paramVo){

        LOGGER.info("call calculateCartOrderPrice ....");

        if (paramVo == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            LOGGER.info("calculateCartOrderPrice CartOrderParamVo : {}",paramVo);
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            CalculateReturnVo calculateReturnVo = calculateService.calculateCartOrderPrice(paramVo,false);
            if (calculateReturnVo != null){
                TbApiUser apiUserByOppenId = tbApiUserMapper.findApiUserByOppenId(paramVo.getOppenId());
                if (apiUserByOppenId != null){
                    calculateReturnVo.setPhoneNum(apiUserByOppenId.getUserBindPhoneNum());
                }
            }
            LOGGER.info(" call cart calculate price calculateReturnVo : {}",calculateReturnVo);
            resultInfo.setData(calculateReturnVo);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateCartOrderPrice happen exception",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/calculateGoodsOrderPrice")
    public ResultInfo calculateGoodsOrderPrice(@RequestBody GoodsOrderParamVo paramVo){

        if (paramVo == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            CalculateReturnVo calculateReturnVo = apiOrderService.calculateGoodsOrderPrice(paramVo,false);
            resultInfo.setData(calculateReturnVo);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",paramVo,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/payUpdateOrder")
    public String payUpdateOrder(HttpServletRequest request){
        LOGGER.info("wechat callback.......");
        String lastXml;
        try {
            boolean flag = apiPayService.payCallbackHandle(request);
            if (flag){
                //处理成功
                lastXml = WxUtil.returnXML("SUCCESS");
                LOGGER.info("wechat pay call back update success *******");
            }else{
                //处理失败
                lastXml =  WxUtil.returnXML("FAIL");
            }
            LOGGER.info("wechat pay callback return info : {}" + lastXml);
            return lastXml;
        } catch (Exception e) {
            lastXml =  WxUtil.returnXML("FAIL");
            LOGGER.info("wechat pay callback return info : {} happen exception" + lastXml,e);
            return lastXml;
        }
    }

    @RequestMapping("/cancelOrder")
    public ResultInfo cancelOrder(@RequestBody Map<String,String> params){
        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String orderId = params.get("orderId");
        LOGGER.info("current user oppenId : {} cancle order orderId : {}",oppenId,orderId);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiOrderService.cancelOrder(oppenId,orderId);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
