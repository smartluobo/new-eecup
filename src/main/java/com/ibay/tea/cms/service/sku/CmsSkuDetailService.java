package com.ibay.tea.cms.service.sku;

import com.ibay.tea.dao.TbSkuDetailMapper;
import com.ibay.tea.entity.TbSkuDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsSkuDetailService {

    @Resource
    private TbSkuDetailMapper tbSkuDetailMapper;

    public List<TbSkuDetail> findAll() {
        return tbSkuDetailMapper.findAll();
    }

    public void addSkuDetail(TbSkuDetail tbSkuDetail) {
        tbSkuDetailMapper.addSkuDetail(tbSkuDetail);

    }

    public void deleteSkuDetail(int id) {
        tbSkuDetailMapper.deleteByPrimaryKey(id);
    }

    public void updateSkuDetail(TbSkuDetail tbSkuDetail) {
        TbSkuDetail dbSkuDetail = tbSkuDetailMapper.selectByPrimaryKey(tbSkuDetail.getId());
        if (dbSkuDetail == null){
            return;
        }
        tbSkuDetailMapper.deleteByPrimaryKey(tbSkuDetail.getId());
        tbSkuDetailMapper.saveUpdateSkuDetail(tbSkuDetail);
    }

    public List<TbSkuDetail> findSkuDetailByTypeId(int typeId) {
        return tbSkuDetailMapper.findSkuDetailByTypeId(typeId);
    }
}
