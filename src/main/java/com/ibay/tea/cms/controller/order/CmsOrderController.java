package com.ibay.tea.cms.controller.order;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.sendMsg.OrderMessageSendService;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.cms.responseVo.OrderStatisticalVo;
import com.ibay.tea.cms.service.order.CmsOrderService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/order")
public class CmsOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsOrderController.class);

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private CmsOrderService cmsOrderService;

    @Resource
    private OrderMessageSendService orderMessageSendService;

    @Resource
    private PrintService printService;

    @Resource
    private StoreCache storeCache;

    @RequestMapping("/orderList/{storeId}/{orderStatus}/{pageSize}/{pageNum}/{takeCode}")
    public ResultInfo orderList(@PathVariable("storeId") int storeId,
                                @PathVariable("orderStatus") int orderStatus,
                                @PathVariable("pageSize") int pageSize,
                                @PathVariable("pageNum") int pageNum,
                                @PathVariable("takeCode") String takeCode){
        try {
            Map<String,Object> condition = new HashMap<>();
            condition.put("storeId",storeId);
            condition.put("orderStatus",orderStatus);
            condition.put("pageSize",pageSize);
            condition.put("startIndex",(pageNum-1)*pageSize);
            condition.put("takeCode",takeCode);
            long total = tbOrderMapper.countByCondition(condition);
            List<TbOrder> orderList = tbOrderMapper.findOrderListByCondition(condition);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setTotal(total);
            resultInfo.setData(orderList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms order list query happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/updateOrder/{storeId}/{orderId}/{orderStatus}")
    public ResultInfo updateOrder(@PathVariable("storeId") int storeId,@PathVariable("orderId") String orderId ,@PathVariable("orderStatus") int orderStatus){
        try {
            LOGGER.info("update order storeId : {},orderId : {} , orderStatus : {}",storeId,orderId,orderStatus);
            if (orderStatus == ApiConstant.ORDER_STATUS_MAKE_COMPLETE || orderStatus == ApiConstant.ORDER_STATUS_CLOSED){
                Map<String,Object> condition = new HashMap<>();
                //执行更新操作
                condition.put("storeId",storeId);
                condition.put("orderStatus",orderStatus);
                condition.put("orderId",orderId);
                tbOrderMapper.updateOrderStatusByCondition(condition);

                //调用接口完成推送
                if (orderStatus == ApiConstant.ORDER_STATUS_MAKE_COMPLETE){
                    LOGGER.info("order make complete message send.....");
                    orderMessageSendService.orderMessageSend(orderId,ApiConstant.ORDER_MAKE_COMPLETE_MESSAGE_SEND);
                }else {
                    LOGGER.info("order close message send.....");
                    orderMessageSendService.orderMessageSend(orderId,ApiConstant.ORDER_CLOSE_MESSAGE_SEND);
                }
                return ResultInfo.newSuccessResultInfo();
            }else{
                return ResultInfo.newEmptyParamsResultInfo();
            }
        }catch (Exception e){
            LOGGER.error("updateOrder happen exception orderId : {} orderStatus : {} storeId : {}",orderId,orderStatus,storeId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/findOrderDetail/{orderId}")
    public ResultInfo findOrderDetail(@PathVariable("orderId") String orderId){

        if (orderId == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbOrderItem> orderItemList = cmsOrderService.findOrderDetail(orderId);
            resultInfo.setData(orderItemList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/orderPrint/{orderId}")
    public ResultInfo orderPrint(@PathVariable("orderId") String orderId){

        if (orderId == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
            TbStore store = storeCache.findStoreById(tbOrder.getStoreId());
            printService.printOrder(tbOrder,store,ApiConstant.PRINT_TYPE_ORDER);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/orderItemPrint/{orderId}/{itemId}")
    public ResultInfo orderItemPrint(@PathVariable("orderId") String orderId,@PathVariable("itemId") int itemId){

        if (orderId == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsOrderService.orderItemPrint(orderId,itemId);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/orderStatistical")
    public ResultInfo orderStatistical(@RequestBody Map<String,Object> condition){

        if (condition == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            OrderStatisticalVo resultVo = cmsOrderService.orderStatistical(condition);
            resultInfo.setData(resultVo);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/turnoverStatistical")
    public ResultInfo turnoverStatistical(@RequestBody Map<String,Object> condition){

        if (condition == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            Map<String,Object> resultMap = cmsOrderService.turnoverStatistical(condition);
            resultInfo.setData(resultMap);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }


}
