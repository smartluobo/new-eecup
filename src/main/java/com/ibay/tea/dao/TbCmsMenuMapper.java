package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCmsMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbCmsMenuMapper {

    TbCmsMenu selectByPrimaryKey(Integer id);

    List<TbCmsMenu> findAll();

    void addMenu(TbCmsMenu tbCmsMenu);

    void deleteMenu(int id);

    void saveUpdateMenu(TbCmsMenu tbCmsMenu);

    List<TbCmsMenu> findByIds(List<String> strings);
}