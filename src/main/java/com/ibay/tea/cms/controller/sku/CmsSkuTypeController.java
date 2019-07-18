package com.ibay.tea.cms.controller.sku;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.sku.CmsSkuTypeService;
import com.ibay.tea.entity.TbSkuType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/skuType")
public class CmsSkuTypeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsSkuTypeController.class);

    @Resource
    private CmsSkuTypeService cmsSkuTypeService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbSkuType> skuTypeList = cmsSkuTypeService.findAll();
            resultInfo.setData(skuTypeList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addSkuType(@RequestBody TbSkuType tbSkuType){

        if (tbSkuType == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsSkuTypeService.addSkuType(tbSkuType);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteSkuType(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsSkuTypeService.deleteSkuType(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateSkuType(@RequestBody TbSkuType tbSkuType){

        if (tbSkuType == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsSkuTypeService.updateSkuType(tbSkuType);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }
}
