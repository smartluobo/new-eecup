package com.ibay.tea.api.controller.sendMsg;

import com.ibay.tea.api.response.ResultInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderMsg")
public class OrderMessageSendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageSendController.class);

    @RequestMapping("/sendMessage/{orderId}")
    public ResultInfo orderMessageSend(@PathVariable("orderId") String orderId){
          if (StringUtils.isEmpty(orderId)){
              return ResultInfo.newEmptyParamsResultInfo();
          }
          try {
              ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

              LOGGER.info("orderMessageSend successful orderId : {}*******",orderId);
              return resultInfo;
          }catch (Exception e){
              LOGGER.error("orderMessageSend happen exception orderId : {}",orderId,e);
              return ResultInfo.newExceptionResultInfo();
          }
    }
}
