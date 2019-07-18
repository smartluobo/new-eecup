package com.ibay.tea.cms.scheduled;

import com.ibay.tea.dao.TbOrderMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.dao.TbUserPayRecordMapper;
import com.ibay.tea.entity.TbOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class OrderCloseScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCloseScheduledTask.class);
    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbUserPayRecordMapper tbUserPayRecordMapper;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void orderCloseTaskMethod(){

        List<TbOrder> orderList = tbOrderMapper.findExpireOrder();
        if (CollectionUtils.isEmpty(orderList)){
            LOGGER.info("current order Close task not found No pay order task end...");
        }
        for (TbOrder tbOrder : orderList) {
            //更新订单为超时关闭
            int count = tbOrderMapper.updateOrderTimeOutClose(tbOrder.getOrderId());
            LOGGER.info("update count : {}",count);
            if (count == 1){
                //如果有优惠券释放优惠券锁定
                if (tbOrder.getUserCouponsId() != 0){
                    tbUserCouponsMapper.updateStatusById(tbOrder.getUserCouponsId(),0);
                }
                Map<String,Object> updateMap = new HashMap<>();
                updateMap.put("payStatus",3);
                updateMap.put("updateTime",new Date());
                updateMap.put("orderId",tbOrder.getOrderId());
                //更新支付记录为支付失败
                tbUserPayRecordMapper.updatePayCloseStatus(updateMap);
            }
        }
        LOGGER.info("current order close task close order count : {}",orderList.size());
    }
}
