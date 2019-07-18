package com.ibay.tea.api.service.goods.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.dao.GoodsMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiGoodsServiceImpl implements ApiGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private ActivityCache activityCache;

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
    public void calculateGoodsPrice(List<TbItem> goodsListByCategoryId, int extraPrice, TodayActivityBean todayActivityBean) {
        if (extraPrice == 0 && todayActivityBean == null){
            return;
        }
        if (todayActivityBean != null){
            //活动类型为全场折扣
            if (todayActivityBean.getTbActivity().getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
                for (TbItem tbItem : goodsListByCategoryId) {
                    calculateActivityPrice(tbItem, extraPrice, todayActivityBean);
                }
            }else {
                if (extraPrice != 0 && !CollectionUtils.isEmpty(goodsListByCategoryId)){
                    for (TbItem tbItem : goodsListByCategoryId) {
                        tbItem.setPrice(tbItem.getPrice() + extraPrice);
                    }
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
                    tbItem.setNum(tbStoreGoods.getGoodsInventory());
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
    public void calculateGoodsPrice(TbItem goodsInfo, int extraPrice, TodayActivityBean todayActivityBean) {
        if (goodsInfo == null){
            return ;
        }
        if (extraPrice == 0 && todayActivityBean == null){
            return;
        }
        if (todayActivityBean != null){
            //活动类型为全场折扣
            if (todayActivityBean.getTbActivity().getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
                calculateActivityPrice(goodsInfo, extraPrice, todayActivityBean);
            }else {
                if (extraPrice != 0){
                    goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
                }
            }
        }else {
            goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
        }
    }

    private void calculateActivityPrice(TbItem goodsInfo, int extraPrice, TodayActivityBean todayActivityBean) {
        goodsInfo.setShowActivityPrice(1);
        goodsInfo.setPrice(goodsInfo.getPrice() + extraPrice);
        TbActivityCouponsRecord tbActivityCouponsRecord = todayActivityBean.getTbActivityCouponsRecordList().get(0);
        TbCoupons tbCoupons = activityCache.getTbCouponsById(tbActivityCouponsRecord.getCouponsId());
        double activityPrice = PriceCalculateUtil.multy(goodsInfo.getPrice(),tbCoupons.getCouponsRatio());
        goodsInfo.setActivityPrice(activityPrice);
    }


}
