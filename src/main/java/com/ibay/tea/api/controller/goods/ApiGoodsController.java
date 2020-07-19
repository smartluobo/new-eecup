package com.ibay.tea.api.controller.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("api/goods")
public class ApiGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;

    @Resource
    private StoreCache storeCache;

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Resource
    private TbItemMapper tbItemMapper;
    @Resource
    private TbItemCatMapper tbItemCatMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private CmsGoodsService cmsGoodsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGoodsController.class);

    @RequestMapping("/listByCategoryId")
    public ResultInfo getGoodsListByCategoryId(@RequestBody Map<String,Long> params){
        LOGGER.info("listByCategoryId params {}",params);
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        Long categoryId = params.get("categoryId");
        Long storeId = params.get("storeId");
        if (categoryId == null || categoryId == 0){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        if (storeId == null){
            storeId = 2L;
        }
        TbStore store = storeCache.findStoreById(storeId.intValue());
        //店铺扩展价格
        int extraPrice = store.getExtraPrice();
        TbActivity fullActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd(), store.getId());
        if (fullActivity== null){
            fullActivity = tbActivityMapper.findTeJiaActivity(String.valueOf(store.getId()),DateUtil.getDateYyyyMMdd());
        }

        //根据店铺信息查看是否存在全场活动
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> goodsListByCategoryId = apiGoodsService.getGoodsListByCategoryId(categoryId);
            //查询店铺信息，商品价格添加店铺扩展价格.和活动信息计算商品价格
            apiGoodsService.calculateGoodsPrice(goodsListByCategoryId,extraPrice,fullActivity);
            apiGoodsService.checkGoodsInventory(goodsListByCategoryId,storeId.intValue());
            resultInfo.setData(goodsListByCategoryId);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getGoodsListByCategoryId happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getGoodsDetailById")
    public ResultInfo getGoodsDetailById(@RequestBody Map<String,Long> params){
        if (CollectionUtils.isEmpty(params)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        Long goodsId = params.get("goodsId");
        Long storeId = params.get("storeId");
        if (goodsId == null || goodsId == 0 ||storeId == null || storeId == 0){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbItem goods = apiGoodsService.getGoodsDetailById(goodsId);
            TbStore store = storeCache.findStoreById(storeId.intValue());
            //店铺扩展价格
            int extraPrice = store.getExtraPrice();
            TbActivity fullActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd(), store.getId());

            apiGoodsService.calculateGoodsPrice(goods,extraPrice,fullActivity);
            apiGoodsService.checkGoodsInventory(goods,storeId.intValue());
            if (goods.getShowActivityPrice() == 1){
                goods.setPrice(goods.getActivityPrice());
            }
            LOGGER.info("getGoodsDetailById goodsId : {}, storeId : {} ,goodsInfo : {}",goodsId,storeId,goods);
            resultInfo.setData(goods);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getGoodsDetailById happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/initStoreGoods/{storeId}")
    public ResultInfo initStoreGoods(@PathVariable("storeId") Long storeId ){
        LOGGER.info("initStoreGoods storeId : {}",storeId);
        if (storeId == null || storeId == 0){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbStore store = tbStoreMapper.selectByPrimaryKey(storeId.intValue());
            tbStoreGoodsMapper.deleteByStoreId(storeId.intValue());
            List<Long> storeCatIds = tbItemCatMapper.findCatIdByStoreId(storeId);
            LOGGER.info("initStoreGoods storeCatIds : {}",storeCatIds);
            List<TbItem> goodsList = tbItemMapper.findGoodsListByCatIds(storeCatIds);
            LOGGER.info("initStoreGoods goodsList size : {}",goodsList.size());
            Date currentDate = new Date();
            List<TbStoreGoods> storeGoodsList = cmsGoodsService.buildStoreGoodsInventory(store, goodsList, currentDate);
            tbStoreGoodsMapper.insertBatch(storeGoodsList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getGoodsDetailById happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
