package com.ibay.tea.api.controller.category;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cache.CategoryCache;
import com.ibay.tea.entity.TbItemCat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/category")
public class ApiCategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCategoryController.class);

    @Resource
    private CategoryCache categoryCache;

    @RequestMapping("list")
    public ResultInfo list(){
        try {
            List<TbItemCat> catList = categoryCache.findAll();
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData(catList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("ApiCategoryController list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
