package com.ibay.tea.cms.controller.sku;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.sku.CmsSkuDetailService;
import com.ibay.tea.cms.service.sku.CmsSkuTypeService;
import com.ibay.tea.entity.TbSkuType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("cms/skuType")
public class CmsSkuTypeController {


    @Resource
    private CmsSkuTypeService cmsSkuTypeService;

    @Resource
    private CmsSkuDetailService cmsSkuDetailService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbSkuType> skuTypeList = cmsSkuTypeService.findAll();
            if (CollectionUtils.isEmpty(skuTypeList)){
                return resultInfo;
            }
            for (TbSkuType tbSkuType : skuTypeList) {
                tbSkuType.setSkuDetails(cmsSkuDetailService.findSkuDetailByTypeId(tbSkuType.getId()));
            }
            resultInfo.setData(skuTypeList);
        	return resultInfo;
        }catch (Exception e){
            log.error("sku type list happen exception ",e);
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addSkuType(@RequestBody TbSkuType tbSkuType){

        if (tbSkuType == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsSkuTypeService.addSkuType(tbSkuType);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteSkuType(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
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
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsSkuTypeService.updateSkuType(tbSkuType);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }
}
