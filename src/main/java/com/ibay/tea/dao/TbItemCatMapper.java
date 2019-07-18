package com.ibay.tea.dao;

import com.ibay.tea.entity.TbItemCat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbItemCatMapper {

    TbItemCat selectByPrimaryKey(Long id);

    List<TbItemCat> findAll();

    void addCategory(TbItemCat tbItemCat);

    void deleteCategoryById(long id);

    void saveUpdateCategory(TbItemCat tbItemCat);
}