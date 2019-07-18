package com.ibay.tea.api.service.pay;

import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbUserPayRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ApiPayService {
    Map<String, Object> createPayOrderToWechat(TbOrder tbOrder) throws Exception;

    Map<String,Object> payByOrderId(String orderId);

    String secondEncrypt(TbUserPayRecord tbUserPayRecord, String timeStamp);

    boolean payCallbackHandle(HttpServletRequest request) throws Exception;

}
