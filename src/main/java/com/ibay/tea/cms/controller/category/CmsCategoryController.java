package com.ibay.tea.cms.controller.category;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.category.CmsCategoryService;
import com.ibay.tea.entity.TbItemCat;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/category")
@Slf4j
public class CmsCategoryController {

    @Resource
    private CmsCategoryService cmsCategoryService;


    @RequestMapping("list")
    public ResultInfo list(int storeId){
        if (storeId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbItemCat> categories = cmsCategoryService.findByStoreId(storeId);
            resultInfo.setData(categories);
            return resultInfo;
        }catch (Exception e){
            log.error(" category find list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/add")
    public ResultInfo addCategory(@RequestBody TbItemCat tbItemCat){
        if (tbItemCat == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsCategoryService.addCategory(tbItemCat);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteCategory(@PathVariable("id") long id){

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
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
            	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            	cmsCategoryService.updateCategory(tbItemCat);
            	return resultInfo;
            }catch (Exception e){
            	return ResultInfo.newExceptionResultInfo();
            }
    }
}
