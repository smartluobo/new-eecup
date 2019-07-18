package com.ibay.tea.cms.service.store;

import com.ibay.tea.entity.TbStore;

import java.util.List;

public interface CmsStoreService {
    List<TbStore> findAll();

    void addStore(TbStore tbStore);

    void deleteStore(int id);

    void updateStore(TbStore tbStore);

    List<TbStore> findByIds(String storeIds);
}
