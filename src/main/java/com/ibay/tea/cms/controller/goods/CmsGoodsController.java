package com.ibay.tea.cms.controller.goods;

import com.ibay.tea.api.response.ResultInfo;

import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.entity.TbItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/goods")
@Slf4j
public class CmsGoodsController {


    @Resource
    private CmsGoodsService cmsGoodsService;

    @PostMapping("/list")
    @ResponseBody
    public ResultInfo listByPage(@RequestBody Map<String,String> params){
       try {
           if (CollectionUtils.isEmpty(params)){
               return ResultInfo.newEmptyParamsResultInfo();
           }
           log.info("cms goods list params : {}",params);
           int pageNum = Integer.valueOf(params.get("pageNum"));
           int pageSize = Integer.valueOf(params.get("pageSize"));
           String title = params.get("title");
           String cid = params.get("cid");
           String storeId = params.get("storeId");

           Map<String,Object> condition = new HashMap<>();
           if (!StringUtils.isEmpty(title) && !"null".equals(title)){
               condition.put("title","%"+title+"%");
           }
           if (!StringUtils.isEmpty(cid) && !"null".equals(cid)){
               condition.put("cid",cid);
           }
           if (!StringUtils.isEmpty(storeId) && !"null".equals(storeId)){
               condition.put("storeId",storeId);
           }
           return cmsGoodsService.findGoodsListByPage(condition,pageNum,pageSize);
       }catch (Exception e){
            log.error("cms goods list happen exception",e);
       	    return ResultInfo.newExceptionResultInfo();
       }

    }

    @RequestMapping("/add")
    public ResultInfo addGoods(@RequestBody TbItem tbItem){
        if (tbItem == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsGoodsService.addGoods(tbItem);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/findGoodsByStore")
    public ResultInfo findGoodsByStore(@RequestBody Map<String,String> params){
        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            String storeId = params.get("storeId");
            Integer storeIdInt = Integer.valueOf(storeId);
            List<TbItem> items = cmsGoodsService.findGoodsByStoreId(storeIdInt);
            resultInfo.setData(items);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteGoods(@PathVariable("id") long id){
        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsGoodsService.deleteGoods(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateGoods(@RequestBody TbItem tbItem){

        if (tbItem == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
        	cmsGoodsService.updateGoods(tbItem);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }




}
