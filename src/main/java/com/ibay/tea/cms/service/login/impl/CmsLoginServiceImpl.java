package com.ibay.tea.cms.service.login.impl;

import com.ibay.tea.cms.service.login.CmsLoginService;
import com.ibay.tea.dao.TbCmsUserMapper;
import com.ibay.tea.entity.TbCmsUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CmsLoginServiceImpl implements CmsLoginService {


    @Resource
    private TbCmsUserMapper tbCmsUserMapper;

    @Override
    public TbCmsUser login(TbCmsUser tbCmsUser) {
        TbCmsUser dbUser = tbCmsUserMapper.findUserByLoginName(tbCmsUser.getLoginName());
        if (dbUser == null){
            return null;
        }
        if (dbUser.getPassword().equals(tbCmsUser.getPassword())){
            return dbUser;
        }
        return null;
    }
}
