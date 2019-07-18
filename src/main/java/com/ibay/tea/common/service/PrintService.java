package com.ibay.tea.common.service;

import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;

import java.util.List;

public interface PrintService {

    void addPrinter(int printerId);

    String printOrder(TbOrder tbOrder, TbStore store ,int printType);

    String queryPrinterStatus(String sn);

    boolean queryOrderPrintStatus(String orderPrintId);

    String printOrderItem(TbOrder tbOrder,TbOrderItem orderItem, TbStore store);

    String printOrderItem(TbOrder tbOrder,List<TbOrderItem> orderItemList, TbStore store);

}
