package com.ibay.tea.cms.service.sku;

import com.ibay.tea.dao.TbSkuTypeMapper;
import com.ibay.tea.entity.TbSkuType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsSkuTypeService {

    @Resource
    private TbSkuTypeMapper tbSkuTypeMapper;

    public List<TbSkuType> findAll() {
        return tbSkuTypeMapper.findAll();
    }

    public void addSkuType(TbSkuType tbSkuType) {
        tbSkuTypeMapper.addSkuType(tbSkuType);

    }

    public void deleteSkuType(int id) {
        tbSkuTypeMapper.deleteByPrimaryKey(id);
    }

    public void updateSkuType(TbSkuType tbSkuType) {
        TbSkuType dbSkuType = tbSkuTypeMapper.selectByPrimaryKey(tbSkuType.getId());
        if (dbSkuType == null){
            return;
        }
        tbSkuTypeMapper.deleteByPrimaryKey(tbSkuType.getId());
        tbSkuTypeMapper.saveUpdateSkuType(tbSkuType);
    }
}
