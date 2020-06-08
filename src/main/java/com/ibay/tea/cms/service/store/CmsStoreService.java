package com.ibay.tea.cms.service.store;

import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class CmsStoreService {

    @Resource
    private TbStoreMapper tbStoreMapper;

    public List<TbStore> findAll() {
        return tbStoreMapper.findAll();
    }

    public void addStore(TbStore tbStore) {
        if (tbStore.getDistributionDistance() == 0){
            tbStore.setDistributionDistance(2000);
        }
        tbStoreMapper.addStore(tbStore);
    }

    public void deleteStore(int id) {
        tbStoreMapper.deleteStore(id);
    }

    public void updateStore(TbStore tbStore) {
        TbStore dbStore = tbStoreMapper.selectByPrimaryKey(tbStore.getId());
        if (dbStore == null){
            return;
        }
        tbStoreMapper.deleteStore(tbStore.getId());
        tbStoreMapper.saveUpdateStore(tbStore);
    }

    public List<TbStore> findByIds(String storeIds) {
        String[] split = storeIds.split(",");
        return tbStoreMapper.findByIds(Arrays.asList(split));
    }
}
