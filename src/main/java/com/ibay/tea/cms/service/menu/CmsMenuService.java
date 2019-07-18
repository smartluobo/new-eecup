package com.ibay.tea.cms.service.menu;

import com.ibay.tea.entity.TbCmsMenu;

import java.util.List;

public interface CmsMenuService {

    List<TbCmsMenu> findAll();

    void addMenu(TbCmsMenu tbCmsMenu);

    void deleteMenu(int id);

    void updateMenu(TbCmsMenu tbCmsMenu);

    List<TbCmsMenu> findByIds(String menuIds);

}
