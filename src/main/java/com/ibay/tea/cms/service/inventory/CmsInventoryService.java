package com.ibay.tea.cms.service.inventory;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.entity.TbStoreGoods;

import java.util.List;
import java.util.Map;

public interface CmsInventoryService {
    ResultInfo findAll(Map<String,String> params);

    void addStoreGoods(TbStoreGoods storeGoods);

    void deleteStoreGoods(int id);

    void updateStoreGoods(TbStoreGoods storeGoods);


    void initStoreGoods(int storeId);

    void clearStoreGoods(int storeId);

}
