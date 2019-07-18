package com.ibay.tea.cms.service.menu.impl;

import com.ibay.tea.cms.service.menu.CmsMenuService;
import com.ibay.tea.dao.TbCmsMenuMapper;
import com.ibay.tea.entity.TbCmsMenu;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CmsMenuServiceImpl implements CmsMenuService {

    @Resource
    private TbCmsMenuMapper tbCmsMenuMapper;

    @Override
    public List<TbCmsMenu> findAll() {
        return tbCmsMenuMapper.findAll();
    }

    @Override
    public void addMenu(TbCmsMenu tbCmsMenu) {
        tbCmsMenuMapper.addMenu(tbCmsMenu);
    }

    @Override
    public void deleteMenu(int id) {
        tbCmsMenuMapper.deleteMenu(id);
    }

    @Override
    public void updateMenu(TbCmsMenu tbCmsMenu) {
        TbCmsMenu dbMenu = tbCmsMenuMapper.selectByPrimaryKey(tbCmsMenu.getId());
        if (dbMenu == null){
            return ;
        }
        tbCmsMenuMapper.deleteMenu(tbCmsMenu.getId());
        tbCmsMenuMapper.saveUpdateMenu(tbCmsMenu);
    }

    @Override
    public List<TbCmsMenu> findByIds(String menuIds) {
        List<TbCmsMenu> menuList;
        if ("-1".equals(menuIds)){
            menuList = tbCmsMenuMapper.findAll();
        }else {
            String[] split = menuIds.split(",");
            menuList = tbCmsMenuMapper.findByIds(Arrays.asList(split));
        }
        return menuList;

    }
}
