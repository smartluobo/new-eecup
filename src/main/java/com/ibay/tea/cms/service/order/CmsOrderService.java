package com.ibay.tea.cms.service.order;

import com.ibay.tea.entity.TbOrderItem;

import java.util.List;

public interface CmsOrderService {
    List<TbOrderItem> findOrderDetail(String orderId);

    void orderItemPrint(String orderId, int itemId);
}
