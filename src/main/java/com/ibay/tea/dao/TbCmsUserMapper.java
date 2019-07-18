package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCmsUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbCmsUserMapper {
    int deleteByPrimaryKey(Integer id);

    TbCmsUser selectByPrimaryKey(Integer id);

    TbCmsUser findUserByLoginName(String loginName);

    List<TbCmsUser> findAll();

    void addCmsUser(TbCmsUser tbCmsUser);

    void saveUpdateCmsUser(TbCmsUser tbCmsUser);
}