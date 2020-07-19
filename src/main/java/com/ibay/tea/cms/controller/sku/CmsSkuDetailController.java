package com.ibay.tea.cms.controller.sku;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.sku.CmsSkuDetailService;
import com.ibay.tea.entity.TbSkuDetail;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/skuDetail")
public class CmsSkuDetailController {

    @Resource
    private CmsSkuDetailService cmsSkuDetailService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbSkuDetail> skuDetailList = cmsSkuDetailService.findAll();
            resultInfo.setData(skuDetailList);
            return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addSkuDetail(@RequestBody TbSkuDetail tbSkuDetail){

        if (tbSkuDetail == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsSkuDetailService.addSkuDetail(tbSkuDetail);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteSkuDetail(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsSkuDetailService.deleteSkuDetail(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateSkuDetail(@RequestBody TbSkuDetail tbSkuDetail){

        if (tbSkuDetail == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsSkuDetailService.updateSkuDetail(tbSkuDetail);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }


}
