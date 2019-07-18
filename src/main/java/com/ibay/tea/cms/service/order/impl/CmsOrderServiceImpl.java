package com.ibay.tea.cms.service.order.impl;

import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.cms.service.order.CmsOrderService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.service.PrintService;
import com.ibay.tea.dao.TbOrderItemMapper;
import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}
