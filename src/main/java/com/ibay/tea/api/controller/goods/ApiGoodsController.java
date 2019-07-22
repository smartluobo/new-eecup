package com.ibay.tea.api.controller.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TodayActivityBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/goods")
public class ApiGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;

    @Resource
    private StoreCache storeCache;

    @Resource
    private ActivityCache activityCache;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGoodsController.class);

    @RequestMapping("/listByCategoryId")
    public ResultInfo getGoodsListByCategoryId(@RequestBody Map<String,Long> params){
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
        TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(storeId.intValue());

        //根据店铺信息查看是否存在全场活动
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> goodsListByCategoryId = apiGoodsService.getGoodsListByCategoryId(categoryId);
            //查询店铺信息，商品价格添加店铺扩展价格.和活动信息计算商品价格
            apiGoodsService.calculateGoodsPrice(goodsListByCategoryId,extraPrice,todayActivityBean);
            apiGoodsService.checkGoodsInventory(goodsListByCategoryId,storeId.intValue());
            resultInfo.setData(goodsListByCategoryId);
            return resultInfo;
        }catch (Exception e){
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
            TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(storeId.intValue());

            apiGoodsService.calculateGoodsPrice(goods,extraPrice,todayActivityBean);
            apiGoodsService.checkGoodsInventory(goods,storeId.intValue());
            if (goods.getShowActivityPrice() == 1){
                goods.setPrice(goods.getActivityPrice());
            }
            LOGGER.info("getGoodsDetailById goodsId : {}, storeId : {} ,goodsInfo : {}",goodsId,storeId,goods);
            resultInfo.setData(goods);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
