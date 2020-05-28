package com.ibay.tea.cms.service.order;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.responseVo.OrderStatisticalVo;
import com.ibay.tea.entity.TbOrderItem;

import java.util.List;
import java.util.Map;

public interface CmsOrderService {

    List<TbOrderItem> findOrderDetail(String orderId);

    void orderItemPrint(String orderId, int itemId);

    OrderStatisticalVo orderStatistical(Map<String, Object> condition);

    Map<String,Object> turnoverStatistical(Map<String, Object> condition);

    ResultInfo findShufuleiByPage(Map<String, Object> condition, int pageNum, int pageSize);

    List<Map<String, Object>> shufuleiCountStatistical(Map<String, Object> condition);
}
