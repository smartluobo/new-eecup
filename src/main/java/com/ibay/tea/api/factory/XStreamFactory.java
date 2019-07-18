package com.ibay.tea.api.factory;

import com.ibay.tea.api.request.WechatCreateOrderRequest;
import com.ibay.tea.api.response.WechatCreateOrderResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XStreamFactory {
    private static XStream requestXstream;
    private static XStream responseXstream;

    static {
       requestXstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
       requestXstream.alias("xml",WechatCreateOrderRequest.class);

        responseXstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        responseXstream.alias("xml",WechatCreateOrderResponse.class);
    }
    /**
     * 用于处理在实体对象中带有_的对象,如果用上述方法，会出现有两个__,导致结果不正确!
     * 属性中有_的属性一定要有改方法
     * @return   返回xstream 对象
     * new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")
     */
    public static XStream getRequestXstream(){
        return requestXstream;
    }


    public static XStream getResponseXstream(){
        return responseXstream;
    }
}
