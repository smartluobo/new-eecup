package com.ibay.tea.cms.service.company.impl;

import com.ibay.tea.cms.service.company.CmsFavorableCompanyService;
import com.ibay.tea.dao.TbFavorableCompanyMapper;
import com.ibay.tea.entity.TbFavorableCompany;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsFavorableCompanyServiceImpl implements CmsFavorableCompanyService {

    @Resource
    private TbFavorableCompanyMapper tbFavorableCompanyMapper;

    @Override
    public List<TbFavorableCompany> findAll() {
        return tbFavorableCompanyMapper.findAll();
    }

    @Override
    public void addFavorableCompany(TbFavorableCompany favorableCompany) {
        tbFavorableCompanyMapper.insert(favorableCompany);
    }

    @Override
    public void deleteFavorableCompany(int id) {
        tbFavorableCompanyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateFavorableCompany(TbFavorableCompany favorableCompany) {
        tbFavorableCompanyMapper.updateByPrimaryKey(favorableCompany);
    }

}
