package com.ibay.tea.dao;

import com.ibay.tea.entity.TbFavorableCompany;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbFavorableCompanyMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TbFavorableCompany record);

    TbFavorableCompany selectByPrimaryKey(Integer id);

    List<TbFavorableCompany> findAll();

    void updateByPrimaryKey(TbFavorableCompany favorableCompany);
}