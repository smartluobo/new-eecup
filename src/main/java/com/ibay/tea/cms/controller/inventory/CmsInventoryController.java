package com.ibay.tea.cms.controller.inventory;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.inventory.CmsInventoryService;
import com.ibay.tea.entity.TbStoreGoods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cms/inventory")
public class CmsInventoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmsInventoryController.class);

    @Resource
    private CmsInventoryService cmsInventoryService;


    @RequestMapping("/list")
    public ResultInfo list(@RequestBody Map<String,String> param){

        try {
           return cmsInventoryService.findAll(param);
        }catch (Exception e){
            LOGGER.error("list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addStoreGoods(@RequestBody TbStoreGoods storeGoods){
        if (storeGoods == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsInventoryService.addStoreGoods(storeGoods);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteStoreGoods(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsInventoryService.deleteStoreGoods(id);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateStoreGoods(@RequestBody TbStoreGoods storeGoods){

        if (storeGoods == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsInventoryService.updateStoreGoods(storeGoods);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/init/{storeId}")
    public ResultInfo initStoreGoods(@PathVariable("storeId") int storeId){

        if (storeId == 0){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsInventoryService.initStoreGoods(storeId);
        	return resultInfo;
        }catch (Exception e){
            LOGGER.error("happen exception ",e);
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/clear/{storeId}")
    public ResultInfo clearStoreGoods(@PathVariable("storeId") int storeId){

        if (storeId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsInventoryService.clearStoreGoods(storeId);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
