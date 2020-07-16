package com.ibay.tea.cms.service.pay;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.system.UserCacheService;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.common.utils.CommonUtil;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbNoPaymentUserMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbNoPaymentUser;
import com.ibay.tea.entity.system.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmsNoPaymentUserService {

    @Resource
    private TbNoPaymentUserMapper tbNoPaymentUserMapper;

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private UserCacheService userCacheService;

    @Resource
    private TbStoreMapper tbStoreMapper;

    public ResultInfo findNoPaymentUserByPage(Map<String, String> params) {
        ResultInfo resultInfo = new ResultInfo();
        Map<String,Object> condition = new HashMap<>();
        int pageNum = Integer.valueOf(params.get("pageNum"));
        int pageSize = Integer.valueOf(params.get("pageSize"));
        String storeId = params.get("storeId");
        int startIndex = (pageNum-1) * pageSize;
        condition.put("startIndex",startIndex);
        condition.put("pageSize",pageSize);
        condition.put("storeId",storeId);
        condition.put("userPhone",params.get("userPhone"));

        long total = tbNoPaymentUserMapper.countByCondition(condition);
        List<TbNoPaymentUser> allByStoreId = tbNoPaymentUserMapper.findAllByCondition(condition);
        resultInfo.setData(allByStoreId);
        resultInfo.setTotal(total);
        return resultInfo;
    }

    public String addNoPaymentUserByPage(Map<String,String> params) {
        SysUser sysUser = userCacheService.getSysUser();
        if (sysUser == null){
            return "当前没有登陆信息";
        }
        String storeId = params.get("storeId");
        String userId = params.get("userId");
        TbApiUser apiUserById = tbApiUserMapper.findApiUserById(Integer.valueOf(userId));

        TbNoPaymentUser tbNoPaymentUser = new TbNoPaymentUser();
        tbNoPaymentUser.setOppenId(apiUserById.getOppenId());
        tbNoPaymentUser.setNickName(apiUserById.getNickName());
        tbNoPaymentUser.setUserPhone(apiUserById.getUserBindPhoneNum());
        tbNoPaymentUser.setStoreId(Integer.valueOf(storeId));
        tbNoPaymentUser.setOperationUser(sysUser.getName());
        tbNoPaymentUser.setOperationUserId(sysUser.getId());
        tbNoPaymentUserMapper.insert(tbNoPaymentUser);
        return CommonConstant.SUCCESS;
    }

    public String deleteNoPaymentUserByPage(int id) {
        tbNoPaymentUserMapper.deleteById(id);
        return CommonConstant.SUCCESS;
    }
}
