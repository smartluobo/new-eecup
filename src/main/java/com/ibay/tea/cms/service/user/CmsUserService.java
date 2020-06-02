package com.ibay.tea.cms.service.user;

import com.ibay.tea.dao.TbCmsUserMapper;
import com.ibay.tea.entity.TbCmsUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsUserService {

    @Resource
    private TbCmsUserMapper tbCmsUserMapper;


    public TbCmsUser findUserById(int id) {
        return tbCmsUserMapper.selectByPrimaryKey(id);
    }


    public List<TbCmsUser> findAll() {
        return tbCmsUserMapper.findAll();
    }


    public void addCmsUser(TbCmsUser tbCmsUser) {
        tbCmsUserMapper.addCmsUser(tbCmsUser);
    }


    public void deleteCmsUser(int id) {
        tbCmsUserMapper.deleteByPrimaryKey(id);
    }

    public void updateCmsUser(TbCmsUser tbCmsUser) {
        TbCmsUser dbCmsUser = tbCmsUserMapper.selectByPrimaryKey(tbCmsUser.getId());
        if (dbCmsUser == null){
            return;
        }
        tbCmsUserMapper.deleteByPrimaryKey(tbCmsUser.getId());
        tbCmsUserMapper.saveUpdateCmsUser(tbCmsUser);
    }
}
