package com.ibay.tea.cms.controller.inventory;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.cms.service.inventory.CmsInventoryService;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TbStoreGoods;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/cms/inventory")
public class CmsInventoryController {

    @Resource
    private CmsInventoryService cmsInventoryService;

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private CmsGoodsService cmsGoodsService;

    @Resource
    private TbStoreMapper tbStoreMapper;


    @RequestMapping("/list")
    public ResultInfo list(@RequestBody Map<String,String> param){

        try {
           return cmsInventoryService.findAll(param);
        }catch (Exception e){
            log.error("list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addStoreGoods(@RequestBody TbStoreGoods storeGoods){
        if (storeGoods == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsInventoryService.addStoreGoods(storeGoods);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteStoreGoods(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
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
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
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
        	ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsInventoryService.initStoreGoods(storeId);
        	return resultInfo;
        }catch (Exception e){
            log.error("happen exception ",e);
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/clear/{storeId}")
    public ResultInfo clearStoreGoods(@PathVariable("storeId") int storeId){

        if (storeId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsInventoryService.clearStoreGoods(storeId);
            return resultInfo;
        }catch (Exception e){
            log.error("happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/initStoreGoods")
    public ResultInfo initStoreGoods(@RequestBody Map<String,String> params){

        if (CollectionUtils.isEmpty(params)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            int storeId = Integer.valueOf(params.get("storeId"));
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            TbStore store = tbStoreMapper.selectByPrimaryKey(storeId);
            if (store == null){
                return ResultInfo.newFailResultInfo("店铺信息为空");
            }
            tbStoreGoodsMapper.deleteByStoreId(storeId);

            List<TbItem> goodsList = tbItemMapper.findGoodsByStoreId(storeId);
            if (CollectionUtils.isEmpty(goodsList)){
                return ResultInfo.newFailResultInfo("当前店铺没有商品信息");
            }
            Date currentDate = new Date();
            List<TbStoreGoods> storeGoodsList = cmsGoodsService.buildStoreGoodsInventory(store, goodsList, currentDate);
            tbStoreGoodsMapper.insertBatch(storeGoodsList);
            return resultInfo;
        }catch (Exception e){
            log.error("getGoodsDetailById happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
