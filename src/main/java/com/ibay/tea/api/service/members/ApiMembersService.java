package com.ibay.tea.api.service.members;

import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.TbFavorableCompanyMapper;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbFavorableCompany;
import com.ibay.tea.entity.TbOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiMembersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiMembersService.class);

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Resource
    private TbFavorableCompanyMapper tbFavorableCompanyMapper;

    public String bindEnterpriseVIP(String oppenId) {
        TbApiUser apiUser = tbApiUserMapper.findApiUserByOppenId(oppenId);
        if (apiUser == null){
            return "你未登陆请先授权微信登陆小程序";
        }
        apiUser.setCompanyId(54);
        apiUser.setCompanyName("TCL集团");
        LOGGER.info("api user bindEnterpriseVIP userId : {},oppenId : {}",apiUser.getId(),oppenId);
        TbFavorableCompany tbFavorableCompany = tbFavorableCompanyMapper.selectByPrimaryKey(54);
        LOGGER.info("tbFavorableCompany : {}",tbFavorableCompany);
        if (tbFavorableCompany == null){
            return "未查询到企业优惠";
        }
        apiUser.setCompanyId(tbFavorableCompany.getId());
        apiUser.setCompanyName(tbFavorableCompany.getCompanyName());
        tbApiUserMapper.bindCompany(apiUser);
        return "绑定企业会员成功";
    }
}
