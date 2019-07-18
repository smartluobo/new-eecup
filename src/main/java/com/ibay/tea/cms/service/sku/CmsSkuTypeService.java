package com.ibay.tea.cms.service.sku;

import com.ibay.tea.entity.TbSkuType;

import java.util.List;

public interface CmsSkuTypeService {

    List<TbSkuType> findAll();

    void addSkuType(TbSkuType tbSkuType);

    void deleteSkuType(int id);

    void updateSkuType(TbSkuType tbSkuType);
}
