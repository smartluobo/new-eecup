package com.ibay.tea.api.service.store;

import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiStoreService {

    @Resource
    private TbStoreMapper tbStoreMapper;

    public List<TbStore> findAll(){
        return tbStoreMapper.findAll();
    }
}
