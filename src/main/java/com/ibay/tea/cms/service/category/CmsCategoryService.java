package com.ibay.tea.cms.service.category;

import com.ibay.tea.dao.TbItemCatMapper;
import com.ibay.tea.entity.TbItemCat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsCategoryService {

    @Resource
    private TbItemCatMapper tbItemCatMapper;

    public List<TbItemCat> findAll() {
        return tbItemCatMapper.findAll();
    }

    public void addCategory(TbItemCat tbItemCat) {
        tbItemCatMapper.addCategory(tbItemCat);
    }

    public void deleteCategoryById(long id) {
        tbItemCatMapper.deleteCategoryById(id);
    }

    public void updateCategory(TbItemCat tbItemCat) {
        TbItemCat dbCategory = tbItemCatMapper.selectByPrimaryKey(tbItemCat.getId());
        if (dbCategory == null){
            return;
        }
        tbItemCatMapper.deleteCategoryById(tbItemCat.getId());
        tbItemCatMapper.saveUpdateCategory(tbItemCat);
    }
}
