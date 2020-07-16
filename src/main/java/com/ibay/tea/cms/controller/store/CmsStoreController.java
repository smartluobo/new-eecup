package com.ibay.tea.cms.controller.store;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.cms.service.store.CmsStoreService;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.entity.TbStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cms/store")
@Slf4j
public class CmsStoreController {

    @Resource
    private CmsStoreService cmsStoreService;

    @Resource
    private CmsGoodsService cmsGoodsService;

    @RequestMapping("/getStoreByUser")
    public ResultInfo getStoreByUser(){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbStore> storeList = cmsStoreService.getStoreByUser();
            resultInfo.setData(storeList);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }


    @RequestMapping("/list/{storeIds}")
    public ResultInfo list(@PathVariable("storeIds") String storeIds){
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbStore> tbStoreList;
        	if ("-1".equals(storeIds)){
                tbStoreList = cmsStoreService.findAll();
            }else {
                tbStoreList = cmsStoreService.findByIds(storeIds);
            }
            resultInfo.setData(tbStoreList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addStore(@RequestBody TbStore tbStore){

        if (tbStore == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsStoreService.addStore(tbStore);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteStore(@PathVariable("id") int id){
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsStoreService.deleteStore(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/update")
    public ResultInfo updateStore(@RequestBody TbStore tbStore){

        if (tbStore == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsStoreService.updateStore(tbStore);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/copyStoreGoods")
    public ResultInfo copyStoreGoods(@RequestBody Map<String,Integer> params){

        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        Integer currentStoreId = params.get("currentStoreId");
        Integer targetStoreId = params.get("targetStoreId");
        if (currentStoreId == null || targetStoreId == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String result = cmsGoodsService.copyStoreGoods(currentStoreId,targetStoreId);
        if (CommonConstant.SUCCESS.equals(result)){
            return new ResultInfo();
        }else {
            return ResultInfo.newFailResultInfo(result);
        }
    }
}
