package com.ibay.tea.api.service.goods.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStoreGoods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ApiGoodsServiceImpl implements ApiGoodsService {

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Override
    public List<TbItem> getGoodsListByCategoryId(long categoryId) {

        List<TbItem> goodsListByCategoryId = goodsCache.getGoodsListByCategoryId(categoryId);

        if (!CollectionUtils.isEmpty(goodsListByCategoryId)){
            List<TbItem> goodsList = new ArrayList<>(goodsListByCategoryId.size());
            for (TbItem tbItem : goodsListByCategoryId) {
                goodsList.add(tbItem.copy());
            }
            return goodsList;
        }

        return null;
    }

    @Override
    public TbItem getGoodsDetailById(long goodsId) {
        TbItem goods = goodsCache.findGoodsById(goodsId);
        //组装sku相关信息
        return goods.copy();
    }

    @Override
    public void calculateGoodsPrice(List<TbItem> goodsListByCategoryId, int extraPrice, TbActivity tbActivity) {
        if (extraPrice == 0 && tbActivity == null){

            return;
        }
        if (tbActivity != null){
            //活动类型为全场折扣
            for (TbItem tbItem : goodsListByCategoryId) {
                calculateActivityPrice(tbItem, extraPrice, tbActivity);
            }
        }else {
            if (!CollectionUtils.isEmpty(goodsListByCategoryId)){
                for (TbItem tbItem : goodsListByCategoryId) {
                    tbItem.setPrice(tbItem.getPrice() + extraPrice);
                }
            }
        }
    }

    @Override
    public void checkGoodsInventory(List<TbItem> goodsList, int storeId) {
        if (CollectionUtils.isEmpty(goodsList)){
            return;
        }
        List<Long> goodsIds = new ArrayList<>(goodsList.size());
        for (TbItem tbItem : goodsList) {
            goodsIds.add(tbItem.getId());
        }
        Map<String ,Object> condition = new HashMap<>();
        condition.put("goodsIds",goodsIds);
        condition.put("storeId",storeId);
        List<TbStoreGoods> storeGoodsList = tbStoreGoodsMapper.findRecordByCondition(condition);
        for (TbItem tbItem : goodsList) {
            for (TbStoreGoods tbStoreGoods : storeGoodsList) {
                if (tbItem.getId().intValue() == tbStoreGoods.getGoodsId()){
                    tbItem.setNum(tbStoreGoods.getGoodsInventory() > 0 ? tbStoreGoods.getGoodsInventory() : 0);
                }
            }
        }
    }

    @Override
    public void checkGoodsInventory(TbItem goodsInfo, int storeId) {
        if (goodsInfo == null){
            return;
        }
        Integer goodsInventory = tbStoreGoodsMapper.findGoodsInventory(goodsInfo.getId(),storeId);
        if (goodsInventory == null){
            goodsInfo.setNum(0);
        }else {
            goodsInfo.setNum(goodsInventory);
        }
    }

    @Override
    public void calculateGoodsPrice(TbItem goodsInfo, int extraPrice, TbActivity tbActivity) {
        if (goodsInfo == null){
            return ;
        }
        if (extraPrice == 0 && tbActivity == null){
            return;
        }
        if (tbActivity != null){
            //活动类型为全场折扣
           calculateActivityPrice(goodsInfo, extraPrice, tbActivity);
            if (extraPrice != 0){
                goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
            }
        }else {
            goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
        }
    }

    private void calculateActivityPrice(TbItem goodsInfo, int extraPrice, TbActivity tbActivity) {
        if (ApiConstant.ACTIVITY_TYPE_TEJIA == tbActivity.getActivityType()){
            String goodsIds = tbActivity.getGoodsIds();
            if (StringUtils.isEmpty(goodsIds)){
                return ;
            }
            List<String> goodsIdList = Arrays.asList(StringUtils.split(goodsIds, ","));
            for (String goodsId : goodsIdList) {
                if (goodsInfo.getId().intValue() == Integer.valueOf(goodsId) && goodsInfo.getTejiaPrice() > 1){
                    goodsInfo.setShowActivityPrice(1);
                    goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
                    goodsInfo.setActivityPrice(goodsInfo.getTejiaPrice() + extraPrice);
                }
            }
        }else {
            goodsInfo.setShowActivityPrice(1);
            goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
            double activityPrice = PriceCalculateUtil.multiply(goodsInfo.getPrice(), tbActivity.getActivityRatio());
            goodsInfo.setActivityPrice(activityPrice);
        }
    }


}
