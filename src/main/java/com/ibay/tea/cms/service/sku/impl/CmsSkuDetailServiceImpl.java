package com.ibay.tea.cms.service.sku.impl;

import com.ibay.tea.cms.service.sku.CmsSkuDetailService;
import com.ibay.tea.dao.TbSkuDetailMapper;
import com.ibay.tea.entity.TbSkuDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsSkuDetailServiceImpl implements CmsSkuDetailService {

    @Resource
    private TbSkuDetailMapper tbSkuDetailMapper;

    @Override
    public List<TbSkuDetail> findAll() {
        return tbSkuDetailMapper.findAll();
    }

    @Override
    public void addSkuDetail(TbSkuDetail tbSkuDetail) {
        tbSkuDetailMapper.addSkuDetail(tbSkuDetail);

    }

    @Override
    public void deleteSkuDetail(int id) {
        tbSkuDetailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateSkuDetail(TbSkuDetail tbSkuDetail) {
        TbSkuDetail dbSkuDetail = tbSkuDetailMapper.selectByPrimaryKey(tbSkuDetail.getId());
        if (dbSkuDetail == null){
            return;
        }
        tbSkuDetailMapper.deleteByPrimaryKey(tbSkuDetail.getId());
        tbSkuDetailMapper.saveUpdateSkuDetail(tbSkuDetail);
    }
}
