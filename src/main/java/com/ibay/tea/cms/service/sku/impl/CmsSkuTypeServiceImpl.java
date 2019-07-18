package com.ibay.tea.cms.service.sku.impl;

import com.ibay.tea.cms.service.sku.CmsSkuTypeService;
import com.ibay.tea.dao.TbSkuTypeMapper;
import com.ibay.tea.entity.TbSkuType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsSkuTypeServiceImpl implements CmsSkuTypeService {

    @Resource
    private TbSkuTypeMapper tbSkuTypeMapper;

    @Override
    public List<TbSkuType> findAll() {
        return tbSkuTypeMapper.findAll();
    }

    @Override
    public void addSkuType(TbSkuType tbSkuType) {
        tbSkuTypeMapper.addSkuType(tbSkuType);

    }

    @Override
    public void deleteSkuType(int id) {
        tbSkuTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateSkuType(TbSkuType tbSkuType) {
        TbSkuType dbSkuType = tbSkuTypeMapper.selectByPrimaryKey(tbSkuType.getId());
        if (dbSkuType == null){
            return;
        }
        tbSkuTypeMapper.deleteByPrimaryKey(tbSkuType.getId());
        tbSkuTypeMapper.saveUpdateSkuType(tbSkuType);
    }
}
