package com.ibay.tea.cms.service.sku;

import com.ibay.tea.entity.TbSkuDetail;

import java.util.List;

public interface CmsSkuDetailService {
    List<TbSkuDetail> findAll();

    void addSkuDetail(TbSkuDetail tbSkuDetail);

    void deleteSkuDetail(int id);

    void updateSkuDetail(TbSkuDetail tbSkuDetail);

}
