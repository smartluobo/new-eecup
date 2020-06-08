package com.ibay.tea.cms.service.company;

import com.ibay.tea.dao.TbFavorableCompanyMapper;
import com.ibay.tea.entity.TbFavorableCompany;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsFavorableCompanyService {

    @Resource
    private TbFavorableCompanyMapper tbFavorableCompanyMapper;

    public List<TbFavorableCompany> findAll() {
        return tbFavorableCompanyMapper.findAll();
    }

    public void addFavorableCompany(TbFavorableCompany favorableCompany) {
        tbFavorableCompanyMapper.insert(favorableCompany);
    }

    public void deleteFavorableCompany(int id) {
        tbFavorableCompanyMapper.deleteByPrimaryKey(id);
    }

    public void updateFavorableCompany(TbFavorableCompany favorableCompany) {
        tbFavorableCompanyMapper.updateByPrimaryKey(favorableCompany);
    }

}
