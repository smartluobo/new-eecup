package com.ibay.tea.cms.service.order.impl;

import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.cms.responseVo.OrderStatisticalVo;
import com.ibay.tea.cms.service.order.CmsOrderService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.dao.TbOrderItemMapper;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CmsOrderServiceImpl implements CmsOrderService {

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private StoreCache storeCache;

    @Resource
    private PrintService printService;
    @Override
    public List<TbOrderItem> findOrderDetail(String orderId) {
        return tbOrderItemMapper.findOrderItemByOrderId(orderId);
    }

    @Override
    public void orderItemPrint(String orderId, int itemId) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        TbStore store = storeCache.findStoreById(tbOrder.getStoreId());
        printService.printOrder(tbOrder,store, ApiConstant.PRINT_TYPE_ORDER_ITEM);
//        if (itemId == -1){
//            List<TbOrderItem> orderItemList = tbOrderItemMapper.findOrderItemByOrderId(orderId);
//            printService.printOrderItem(tbOrder,orderItemList,store);
//        }else {
//            TbOrderItem orderItem = tbOrderItemMapper.selectByPrimaryKey(itemId);
//            if (orderItem != null){
//
//                printService.printOrderItem(tbOrder,orderItem,store);
//            }
//        }
    }

    @Override
    public OrderStatisticalVo orderStatistical(Map<String, Object> condition) {

        List<Map<String, Integer>> maps = tbOrderMapper.orderStatistical(condition);
        if (CollectionUtils.isEmpty(maps)){
            return null;
        }
        OrderStatisticalVo orderStatisticalVo = new OrderStatisticalVo();

        for (Map<String, Integer> map : maps) {
            if (map.get("status") == 0){
                orderStatisticalVo.setNoPayOrderCount(map.get("orderCount"));
            } else if (map.get("status") == 1){
                orderStatisticalVo.setPayOrderCount(map.get("orderCount"));
            } else if (map.get("status") == 2){
                orderStatisticalVo.setCompletedCount(map.get("orderCount"));
            } else if (map.get("status") == 3){
                orderStatisticalVo.setCloseOrderCount(map.get("orderCount"));
            } else if (map.get("status") == 4){
                orderStatisticalVo.setTimeOutOrderCount(map.get("orderCount"));
            } else if (map.get("status") == 5){
                orderStatisticalVo.setCancelOrderCount(map.get("orderCount"));
            } else if (map.get("status") == 8){
                orderStatisticalVo.setProxyOrderCount(map.get("orderCount"));
            }
        }
        return orderStatisticalVo;
    }

    @Override
    public Map<String, Object> turnoverStatistical(Map<String, Object> condition) {
        return tbOrderMapper.turnoverStatistical(condition);
    }
}
