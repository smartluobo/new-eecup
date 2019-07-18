package com.ibay.tea.cms.service.category.impl;

import com.ibay.tea.cms.service.category.CmsCategoryService;
import com.ibay.tea.dao.TbItemCatMapper;
import com.ibay.tea.entity.TbItemCat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsCategoryServiceImpl implements CmsCategoryService {

    @Resource
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> findAll() {
        return tbItemCatMapper.findAll();
    }

    @Override
    public void addCategory(TbItemCat tbItemCat) {
        tbItemCatMapper.addCategory(tbItemCat);
    }

    @Override
    public void deleteCategoryById(long id) {
        tbItemCatMapper.deleteCategoryById(id);
    }

    @Override
    public void updateCategory(TbItemCat tbItemCat) {
        TbItemCat dbCategory = tbItemCatMapper.selectByPrimaryKey(tbItemCat.getId());
        if (dbCategory == null){
            return;
        }
        tbItemCatMapper.deleteCategoryById(tbItemCat.getId());
        tbItemCatMapper.saveUpdateCategory(tbItemCat);
    }
}
