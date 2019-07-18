package com.ibay.tea.cms.service.store.impl;

import com.ibay.tea.cms.service.store.CmsStoreService;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class CmsStoreServiceImpl implements CmsStoreService {

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Override
    public List<TbStore> findAll() {
        return tbStoreMapper.findAll();
    }

    @Override
    public void addStore(TbStore tbStore) {
        if (tbStore.getDistributionDistance() == 0){
            tbStore.setDistributionDistance(2000);
        }
        tbStoreMapper.addStore(tbStore);
    }

    @Override
    public void deleteStore(int id) {
        tbStoreMapper.deleteStore(id);
    }

    @Override
    public void updateStore(TbStore tbStore) {
        TbStore dbStore = tbStoreMapper.selectByPrimaryKey(tbStore.getId());
        if (dbStore == null){
            return;
        }
        tbStoreMapper.deleteStore(tbStore.getId());
        tbStoreMapper.saveUpdateStore(tbStore);
    }

    @Override
    public List<TbStore> findByIds(String storeIds) {
        String[] split = storeIds.split(",");
        return tbStoreMapper.findByIds(Arrays.asList(split));
    }
}
