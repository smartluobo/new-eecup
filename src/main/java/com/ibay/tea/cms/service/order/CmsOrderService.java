package com.ibay.tea.cms.service.order;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.cms.responseVo.OrderStatisticalVo;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.dao.TbOrderItemMapper;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CmsOrderService {

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private StoreCache storeCache;

    @Resource
    private PrintService printService;

    public List<TbOrderItem> findOrderDetail(String orderId) {
        return tbOrderItemMapper.findOrderItemByOrderId(orderId);
    }

    public void orderItemPrint(String orderId, int itemId) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        TbStore store = storeCache.findStoreById(tbOrder.getStoreId());
        printService.printOrder(tbOrder,store, ApiConstant.PRINT_TYPE_ORDER_ITEM);
    }

    public OrderStatisticalVo orderStatistical(Map<String, Object> condition) {

        List<Map<String, Object>> maps = tbOrderMapper.orderStatistical(condition);
        if (CollectionUtils.isEmpty(maps)){
            return null;
        }
        OrderStatisticalVo orderStatisticalVo = new OrderStatisticalVo();

        for (Map<String, Object> map : maps) {
            String orderCount = map.get("orderCount").toString();
            log.error("**************orderCount : {}************",orderCount);
            Integer status = Integer.valueOf(map.get("status").toString());
            log.error("**************orderCount : {}************",orderCount);
            if (status == 0){
                orderStatisticalVo.setNoPayOrderCount(Integer.valueOf(orderCount));
            } else if (status == 1){
                orderStatisticalVo.setPayOrderCount(Integer.valueOf(orderCount));
            } else if (status == 2){
                orderStatisticalVo.setCompletedCount(Integer.valueOf(orderCount));
            } else if (status == 3){
                orderStatisticalVo.setCloseOrderCount(Integer.valueOf(orderCount));
            } else if (status == 4){
                orderStatisticalVo.setTimeOutOrderCount(Integer.valueOf(orderCount));
            } else if (status == 5){
                orderStatisticalVo.setCancelOrderCount(Integer.valueOf(orderCount));
            } else if (status == 8){
                orderStatisticalVo.setProxyOrderCount(Integer.valueOf(orderCount));
            }
        }
        return orderStatisticalVo;
    }

    public Map<String, Object> turnoverStatistical(Map<String, Object> condition) {
        return tbOrderMapper.turnoverStatistical(condition);
    }

    public ResultInfo findShufuleiByPage(Map<String, Object> condition, int pageNum, int pageSize) {
        ResultInfo resultInfo = new ResultInfo();
        long total = tbOrderItemMapper.countShufuleiCondition(condition);
        int startIndex = (pageNum-1) * pageSize;
        condition.put("startIndex",startIndex);
        condition.put("pageSize",pageSize);

        List<TbOrderItem> orderItems = tbOrderItemMapper.findShufuleiListByPage(condition);
        resultInfo.setData(orderItems);
        resultInfo.setTotal(total);
        return resultInfo;
    }

    public List<Map<String, Object>> shufuleiCountStatistical(Map<String, Object> condition) {
        return tbOrderItemMapper.shufuleiCountStatistical(condition);
    }
}
