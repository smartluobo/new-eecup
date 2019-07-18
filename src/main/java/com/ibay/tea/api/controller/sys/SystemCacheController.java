package com.ibay.tea.api.controller.sys;

import com.ibay.tea.cache.CategoryCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/categoryCache")
public class SystemCacheController {

    @Resource
    private CategoryCache categoryCache;

    @RequestMapping("/refresh")
    public Object refreshCategoryCache(){
        categoryCache.refreshCategoryCache();
        return "success";
    }
}
