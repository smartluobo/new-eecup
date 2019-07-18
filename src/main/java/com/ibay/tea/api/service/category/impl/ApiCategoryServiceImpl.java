package com.ibay.tea.api.service.category.impl;

import com.ibay.tea.api.service.category.ApiCategoryService;
import com.ibay.tea.dao.CategoryMapper;
import com.ibay.tea.dao.TbItemCatMapper;
import com.ibay.tea.entity.TbItemCat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiCategoryServiceImpl implements ApiCategoryService {

    @Resource
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> findAll() {
        return tbItemCatMapper.findAll();
    }
}
