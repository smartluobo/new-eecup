package com.ibay.tea.cms.controller.sku;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.sku.CmsSkuDetailService;
import com.ibay.tea.entity.TbSkuDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/skuDetail")
public class CmsSkuDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsSkuDetailController.class);

    @Resource
    private CmsSkuDetailService cmsSkuDetailService;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
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
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsSkuDetailService.addSkuDetail(tbSkuDetail);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteSkuDetail(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
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
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsSkuDetailService.updateSkuDetail(tbSkuDetail);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }


}
