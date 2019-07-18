package com.ibay.tea.cms.service.inventory;

import com.ibay.tea.entity.TbStoreGoods;

import java.util.List;

public interface CmsInventoryService {
    List<TbStoreGoods> findAll(int storeId);

    void addStoreGoods(TbStoreGoods storeGoods);

    void deleteStoreGoods(int id);

    void updateStoreGoods(TbStoreGoods storeGoods);


    void initStoreGoods(int storeId);

}
