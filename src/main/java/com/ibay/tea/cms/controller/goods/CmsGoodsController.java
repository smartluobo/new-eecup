package com.ibay.tea.cms.controller.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.common.utils.PageUtil;
import com.ibay.tea.entity.TbItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CmsGoodsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsGoodsController.class);

    @Resource
    private CmsGoodsService cmsGoodsService;

    @PostMapping("/list")
    @ResponseBody
    public ResultInfo listByPage(@RequestBody Map<String,String> params){
       try {
           if (CollectionUtils.isEmpty(params)){
               return ResultInfo.newEmptyParamsResultInfo();
           }
           LOGGER.info("cms goods list params : {}",params);
       	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

           int pageNum = Integer.valueOf(params.get("pageNum"));
           int pageSize = Integer.valueOf(params.get("pageSize"));
           String title = params.get("title");
           String cid = params.get("cid");
           LOGGER.info("title: {},cid : {},pageNum: {} ,pageSize : {}",title,cid,pageNum, pageSize);
           Map<String,Object> condition = new HashMap<>();
           if (!StringUtils.isEmpty(title) && !"null".equals(title)){
               condition.put("title","%"+title+"%");
           }
           if (!StringUtils.isEmpty(cid) && !"null".equals(cid)){
               condition.put("cid",cid);
           }
           LOGGER.info("cms goods list condition : {}",condition);

           return cmsGoodsService.findGoodsListByPage(condition,pageNum,pageSize);


       }catch (Exception e){
            LOGGER.error("cms goods list happen exception",e);
       	    return ResultInfo.newExceptionResultInfo();
       }

    }

    @RequestMapping("/add")
    public ResultInfo addGoods(@RequestBody TbItem tbItem){

        if (tbItem == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsGoodsService.addGoods(tbItem);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteGoods(@PathVariable("id") long id){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
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
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsGoodsService.updateGoods(tbItem);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }



}
