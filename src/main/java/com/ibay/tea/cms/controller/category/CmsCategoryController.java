package com.ibay.tea.cms.controller.category;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.category.CmsCategoryService;
import com.ibay.tea.entity.Category;
import com.ibay.tea.entity.TbItemCat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/category")
public class CmsCategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCategoryController.class);

    @Resource
    private CmsCategoryService cmsCategoryService;

    @RequestMapping("list")
    public ResultInfo list(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItemCat> categories = cmsCategoryService.findAll();
            resultInfo.setData(categories);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error(" category find list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/add")
    public ResultInfo addCategory(@RequestBody TbItemCat tbItemCat){
        if (tbItemCat == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsCategoryService.addCategory(tbItemCat);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteCategory(@PathVariable("id") long id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsCategoryService.deleteCategoryById(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateCategory( @RequestBody TbItemCat tbItemCat){

            if (tbItemCat == null){
            	return ResultInfo.newEmptyParamsResultInfo();
            }
            try {
            	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            	cmsCategoryService.updateCategory(tbItemCat);
            	return resultInfo;
            }catch (Exception e){
            	return ResultInfo.newExceptionResultInfo();
            }
    }
}
