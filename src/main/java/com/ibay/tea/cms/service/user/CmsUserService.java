package com.ibay.tea.cms.service.user;

import com.ibay.tea.entity.TbCmsUser;

import java.util.List;

public interface CmsUserService {
    TbCmsUser findUserById(int id);

    List<TbCmsUser> findAll();

    void addCmsUser(TbCmsUser tbCmsUser);

    void deleteCmsUser(int id);

    void updateCmsUser(TbCmsUser tbCmsUser);
}
