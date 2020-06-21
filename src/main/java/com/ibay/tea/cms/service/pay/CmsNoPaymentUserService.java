package com.ibay.tea.cms.service.pay;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.common.utils.CommonUtil;
import com.ibay.tea.dao.TbNoPaymentUserMapper;
import com.ibay.tea.entity.TbNoPaymentUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmsNoPaymentUserService {

    @Resource
    private TbNoPaymentUserMapper tbNoPaymentUserMapper;

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

    public String addNoPaymentUserByPage(TbNoPaymentUser tbNoPaymentUser) {
        tbNoPaymentUserMapper.insert(tbNoPaymentUser);
        return CommonConstant.SUCCESS;
    }

    public String deleteNoPaymentUserByPage(int id) {
        tbNoPaymentUserMapper.deleteById(id);
        return CommonConstant.SUCCESS;
    }
}
