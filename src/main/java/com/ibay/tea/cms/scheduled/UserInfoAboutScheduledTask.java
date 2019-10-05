package com.ibay.tea.cms.scheduled;

import com.ibay.tea.dao.*;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbApiUserAddress;
import com.ibay.tea.entity.TbOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
@EnableScheduling
public class UserInfoAboutScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoAboutScheduledTask.class);

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private TbApiUserAddressMapper tbApiUserAddressMapper;

    @Scheduled(cron = "0 45 23 * * ?")
    public void userPhoneBindTask(){
        List<TbApiUser> userList = tbApiUserMapper.findNoBindPhoneUser();
        if (CollectionUtils.isEmpty(userList)){
            LOGGER.info("current not found No bind phone user info ...");
            return ;
        }
        for (TbApiUser userInfo : userList) {
            try {
                TbOrder order =tbOrderMapper.findSendOrderByOppenId(userInfo.getOppenId());
                if (order != null){
                    TbApiUserAddress tbApiUserAddress = tbApiUserAddressMapper.selectByPrimaryKey(order.getUserAddressId());
                    tbApiUserMapper.updateApiUserPhone(userInfo.getOppenId(),tbApiUserAddress.getPhoneNum());
                }
            }catch (Exception e){
                LOGGER.error("update current user : {} phone happen exception ",userInfo,e);
            }
        }
        LOGGER.info("current No bind phone user  count : {}",userList.size());
    }

    @Scheduled(cron = "0 45 23 * * ?")
    public void userStoreBindTask(){
        List<TbApiUser> userList = tbApiUserMapper.findNoBindStoreUser();
        if (CollectionUtils.isEmpty(userList)){
            LOGGER.info("current not found No bind phone user info ...");
            return ;
        }
        for (TbApiUser userInfo : userList) {
            try {
                List<TbOrder> orderList =tbOrderMapper.findOrderByOppenId(userInfo.getOppenId());
                if (!CollectionUtils.isEmpty(orderList)){
                    tbApiUserMapper.updateApiUserStore(userInfo.getOppenId(),orderList.get(0).getStoreId());
                }
            }catch (Exception e){
                LOGGER.error("update current user : {} phone happen exception ",userInfo,e);
            }
        }
        LOGGER.info("current No bind store user  count : {}",userList.size());
    }
}
