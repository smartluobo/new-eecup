package com.ibay.tea.cms.service.sms;

import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.dao.TbSmsConfigMapper;
import com.ibay.tea.entity.TbSmsConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class CmsSmsService {

    @Resource
    private TbSmsConfigMapper tbSmsConfigMapper;

    @Resource
    private SendSmsService sendSmsService;

    @Resource(name = "sendExecutorService")
    private ExecutorService sendExecutorService;

    public List<TbSmsConfig> findAll() {
        return tbSmsConfigMapper.findAll();

    }

    public void addSmsConfig(TbSmsConfig smsConfig) {
        tbSmsConfigMapper.insert(smsConfig);
    }

    public void deleteSmsConfig(int id) {
        tbSmsConfigMapper.selectByPrimaryKey(id);
    }

    public void updateSmsConfig(TbSmsConfig smsConfig) {
        tbSmsConfigMapper.updateByPrimaryKey(smsConfig);
    }

    public void sendSms(int id) {
        TbSmsConfig tbSmsConfig = tbSmsConfigMapper.selectByPrimaryKey(id);
        if (tbSmsConfig == null){
            return;
        }

        sendExecutorService.execute(() -> sendSmsService.sendSmsContent(tbSmsConfig));
    }
}
