package com.ibay.tea.api.service.store.impl;

import com.ibay.tea.api.service.store.ApiStoreService;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiStoreServiceImpl implements ApiStoreService {

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Override
    public List<TbStore> findAll(){
        return tbStoreMapper.findAll();
    }
}
