package com.ibay.tea.cms.service.charge;

import com.ibay.tea.cms.service.system.UserCacheService;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.dao.charge.UserAccountMapper;
import com.ibay.tea.dao.charge.UserAccountRecordMapper;
import com.ibay.tea.dao.charge.UserChargeMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.charge.TbChargeConfig;
import com.ibay.tea.entity.charge.UserAccount;
import com.ibay.tea.entity.charge.UserAccountRecord;
import com.ibay.tea.entity.system.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CmsUserChargeService {

    @Resource
    private UserChargeMapper userChargeMapper;

    @Resource
    private UserCacheService userCacheService;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserAccountRecordMapper userAccountRecordMapper;

    public List<TbChargeConfig> findAllChargeConfig() {
        return userChargeMapper.findAllChargeConfig();
    }

    public String userCharge(Map<String, String> params) {
        SysUser sysUser = userCacheService.getSysUser();
        int chargeConfigId = params.get("chargeConfigId") == null ? -1 : Integer.valueOf(params.get("chargeConfigId"));
        int apiUserId = params.get("apiUserId") == null ? -1 : Integer.valueOf(params.get("apiUserId"));
        int storeId = params.get("storeId") == null ? -1 : Integer.valueOf(params.get("storeId"));

        TbChargeConfig chargeConfig = userChargeMapper.findChargeConfigById(chargeConfigId);

        TbApiUser apiUserInfo = tbApiUserMapper.findApiUserById(apiUserId);

        TbStore storeInfo = tbStoreMapper.selectByPrimaryKey(storeId);

        if (sysUser ==null || chargeConfig == null || apiUserInfo == null || storeInfo == null){
            return "必要参数结果为空";
        }

        //验证用户是否第一次充值
        UserAccount userAccountInfo = userAccountMapper.findByApiUserId(apiUserId);
        if (userAccountInfo == null){
            //表示用户第一充值费
            UserAccount account = new UserAccount();
            account.setApiUserId(apiUserId);
            account.setUserPhone(apiUserInfo.getUserBindPhoneNum());
            account.setOppenId(apiUserInfo.getOppenId());
            account.setNickName(apiUserInfo.getNickName());
            int chargeAmount = chargeConfig.getChargeAmount();
            int giveAmount = chargeConfig.getGiveAmount();

            account.setAccountAmount(String.valueOf(chargeAmount + giveAmount));
            account.setFirstStoreId(storeId);
            account.setCmsUserId(sysUser.getId());
            account.setCmsUserName(sysUser.getName());
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            account.setLastChargeTime(new Date());
            userAccountMapper.insert(account);
            //构建账户变动记录并保存
            UserAccountRecord record = new UserAccountRecord();
            record.setType(1);
            record.setAmount(account.getAccountAmount());
            record.setStoreId(storeId);
            record.setPreAmount("0");
            record.setAfterAmount(account.getAccountAmount());
            record.setCmsUserId(sysUser.getId());
            record.setCmsUserName(sysUser.getName());
            record.setCreateTime(new Date());
            userAccountRecordMapper.insert(record);
        }else{
            //修改充值信息 新增账户变动记录
        }
        return CommonConstant.SUCCESS;
    }
}
