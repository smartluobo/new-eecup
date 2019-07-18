package com.ibay.tea.cms.service.category;

import com.ibay.tea.entity.TbItemCat;

import java.util.List;

public interface CmsCategoryService {
    List<TbItemCat> findAll();

    void addCategory(TbItemCat tbItemCat);

    void deleteCategoryById(long id);

    void updateCategory(TbItemCat tbItemCat);

}
