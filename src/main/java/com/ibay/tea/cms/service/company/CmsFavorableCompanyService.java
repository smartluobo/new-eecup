package com.ibay.tea.cms.service.company;

import com.ibay.tea.entity.TbFavorableCompany;

import java.util.List;

public interface CmsFavorableCompanyService {
    List<TbFavorableCompany> findAll();

    void addFavorableCompany(TbFavorableCompany favorableCompany);

    void deleteFavorableCompany(int id);

    void updateFavorableCompany(TbFavorableCompany favorableCompany);
}
