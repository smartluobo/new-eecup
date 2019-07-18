package com.ibay.tea.cache;

import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbSkuDetailMapper;
import com.ibay.tea.dao.TbSkuTypeMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbSkuDetail;
import com.ibay.tea.entity.TbSkuType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsCache implements InitializingBean {

    private Map<Long,List<TbItem>> categoryGoodsCacheMap;

    private Map<Long,TbItem> goodsIdCacheMap;

    private List<TbSkuType> tbSkuTypeCache;

    private List<TbSkuDetail> tbSkuDetailCache;

    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private TbSkuTypeMapper tbSkuTypeMapper;

    @Resource
    private TbSkuDetailMapper tbSkuDetailMapper;

    @Resource
    private ActivityCache activityCache;

    private void initGoodsCacheMap(){
        initSkuTypeCache();
        initSkuDetailCache();
        List<TbItem> goodsList = tbItemMapper.findAll();
        categoryGoodsCacheMap = new HashMap<>();
        goodsIdCacheMap = new HashMap<>();
        for (TbItem tbItem : goodsList) {
            List<TbItem> categoryTbItems = categoryGoodsCacheMap.get(tbItem.getCid());
            String skuTypeIds = tbItem.getSkuTypeIds();
            String defaultSkuDetailIds = tbItem.getDefaultSkuDetailIds();

            if (skuTypeIds != null && skuTypeIds.trim().length() > 0){
                setGoodsSkuShowInfo(skuTypeIds,defaultSkuDetailIds,tbItem);
            }
            if (categoryTbItems == null){
                List<TbItem> categoryGoodsList = new ArrayList<>();
                categoryGoodsList.add(tbItem);
                categoryGoodsCacheMap.put(tbItem.getCid(),categoryGoodsList);
            }else {
                categoryTbItems.add(tbItem);
            }
            goodsIdCacheMap.put(tbItem.getId(),tbItem);
        }
    }

    private void initSkuTypeCache() {
        tbSkuTypeCache = tbSkuTypeMapper.findAll();
    }

    private void initSkuDetailCache(){
        tbSkuDetailCache =tbSkuDetailMapper.findAll();
    }

    private void setGoodsSkuShowInfo(String skuTypeIds,String defaultSkuDetailIds, TbItem tbItem) {
        if (StringUtils.isEmpty(skuTypeIds)){
            return;
        }
        List<TbSkuType> skuTypes = new ArrayList<>();
        String[] skuTypeIdArr = skuTypeIds.split(",");
        String[] defaultSkuDetailIdArr = defaultSkuDetailIds.split(",");

        for (String skuTypeId : skuTypeIdArr) {
            for (TbSkuType tbSkuType : tbSkuTypeCache) {
                if (tbSkuType.getId() == Long.parseLong(skuTypeId)){
                    skuTypes.add(tbSkuType);
                    //设置skuType下的sku_detail
                    List<TbSkuDetail> skuDetails = new ArrayList<>();
                    for (TbSkuDetail tbSkuDetail : tbSkuDetailCache) {
                        TbSkuDetail copySkuDetail = tbSkuDetail.copy();
                        if (tbSkuDetail.getSkuTypeId() == tbSkuType.getId()){
                            skuDetails.add(copySkuDetail);
                        }
                        for (String s : defaultSkuDetailIdArr) {
                            if (copySkuDetail.getId().intValue() == Integer.valueOf(s).intValue()){
                                copySkuDetail.setIsSelected(1);
                            }
                        }
                    }
                    tbSkuType.setSkuDetails(skuDetails);
                }
            }
        }
        tbItem.setSkuShowInfos(skuTypes);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initGoodsCacheMap();
    }

    public List<TbItem> getGoodsListByCategoryId(long categoryId) {
        return categoryGoodsCacheMap.get(categoryId);
    }

    public TbItem findGoodsById(long goodsId) {
        return goodsIdCacheMap.get(goodsId);
    }

    public int calculateSkuPrice(String skuDetailIds) {
        int skuPrice = 0;
        String[] skuDetailIdArr = skuDetailIds.split(",");
        for (String s : skuDetailIdArr) {
            for (TbSkuDetail tbSkuDetail : tbSkuDetailCache) {
                if (tbSkuDetail.getId().intValue() == Integer.valueOf(s)){
                    skuPrice += tbSkuDetail.getSkuDetailPrice();
                }
            }
        }
        return skuPrice;
    }
}
