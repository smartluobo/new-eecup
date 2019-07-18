package com.ibay.tea.cms.service.user.impl;

import com.ibay.tea.cms.service.user.CmsUserService;
import com.ibay.tea.dao.TbCmsUserMapper;
import com.ibay.tea.dao.UserMapper;
import com.ibay.tea.entity.TbCmsUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsUserServiceImpl implements CmsUserService{

    @Resource
    private TbCmsUserMapper tbCmsUserMapper;

    @Override
    public TbCmsUser findUserById(int id) {
        return tbCmsUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TbCmsUser> findAll() {
        return tbCmsUserMapper.findAll();
    }

    @Override
    public void addCmsUser(TbCmsUser tbCmsUser) {
        tbCmsUserMapper.addCmsUser(tbCmsUser);
    }

    @Override
    public void deleteCmsUser(int id) {
        tbCmsUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateCmsUser(TbCmsUser tbCmsUser) {
        TbCmsUser dbCmsUser = tbCmsUserMapper.selectByPrimaryKey(tbCmsUser.getId());
        if (dbCmsUser == null){
            return;
        }
        tbCmsUserMapper.deleteByPrimaryKey(tbCmsUser.getId());
        tbCmsUserMapper.saveUpdateCmsUser(tbCmsUser);
    }
}
