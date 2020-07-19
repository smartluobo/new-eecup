package com.ibay.tea.api.service.category;

import com.ibay.tea.dao.TbItemCatMapper;
import com.ibay.tea.entity.TbItemCat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiCategoryService {

    @Resource
    private TbItemCatMapper tbItemCatMapper;

    public List<TbItemCat> findAll() {
        return tbItemCatMapper.findAll();
    }

    public List<TbItemCat> findByStoreId(String storeId) {
        return tbItemCatMapper.findByStoreId(storeId);
    }
}
