package com.ibay.tea.cache;

import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StoreCache implements InitializingBean {

    @Resource
    private TbStoreMapper tbStoreMapper;

    private List<TbStore> storeList;

    private Map<Integer,TbStore> storeMap;

    private void initStoreCache(){
        storeList = tbStoreMapper.findAll();
        if (!CollectionUtils.isEmpty(storeList)){
            for (TbStore tbStore : storeList) {
                if (storeMap == null){
                    storeMap = new HashMap<>();
                }
                storeMap.put(tbStore.getId(),tbStore);
            }
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        initStoreCache();
    }

    public List<TbStore> getStoreList() {
        return storeList;
    }

    public TbStore findStoreById(int id){
        return storeMap.get(id);
    }
}
