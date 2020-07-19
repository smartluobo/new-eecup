package com.ibay.tea.cms.service.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.dao.TbItemCatMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbItemCat;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TbStoreGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class CmsGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;
    @Resource
    private TbItemCatMapper tbItemCatMapper;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    public ResultInfo findGoodsListByPage(Map<String,Object> condition, Integer pageNum, Integer pageSize) {
        ResultInfo resultInfo = new ResultInfo();
        long total = countGoodsByCondition(condition);
        int startIndex = (pageNum-1) * pageSize;
        condition.put("startIndex",startIndex);
        condition.put("pageSize",pageSize);
        List<TbItem> goodsListByPage = tbItemMapper.findGoodsListByPage(condition);
        resultInfo.setData(goodsListByPage);
        resultInfo.setTotal(total);
        return resultInfo;
    }

    public long countGoodsByCondition(Map<String,Object> condition){
        return tbItemMapper.countGoodsByCondition(condition);
    }

    public void updateGoods(TbItem tbItem) {

        TbItem dbGoods = tbItemMapper.selectByPrimaryKey(tbItem.getId());
        if (dbGoods == null){
            return;
        }
        tbItemMapper.deleteGoods(tbItem.getId());
        tbItemMapper.saveUpdateGoods(tbItem);

    }

    public void deleteGoods(long id) {
        tbItemMapper.deleteGoods(id);
    }

    public void addGoods(TbItem tbItem) {
        tbItemMapper.addGoods(tbItem);
    }

    /**
     *
     * @param currentStoreId 当前店铺
     * @param targetStoreId 目标店铺
     * @return
     */
    @Transactional
    public String copyStoreGoods(Integer currentStoreId, Integer targetStoreId) {
        Long currentStoreGoodsCount = tbItemMapper.countGoodsByStoreId(currentStoreId);
        if (currentStoreGoodsCount > 0){
            return "当前店铺已经存在商品，不支持该功能";
        }

        List<TbItemCat> targetItemCatList = tbItemCatMapper.findByStoreId(String.valueOf(targetStoreId));
        if (CollectionUtils.isEmpty(targetItemCatList)){
            return "目标店铺分类为空，不支持复制功能";
        }

        //初始化店铺分类
        for (TbItemCat tbItemCat : targetItemCatList) {
            tbItemCat.setStoreId(String.valueOf(currentStoreId));
            tbItemCat.setCreated(new Date());
            tbItemCat.setUpdated(new Date());
            tbItemCatMapper.addCategory(tbItemCat);
        }
        List<TbItemCat> currentItemCatList = tbItemCatMapper.findByStoreId(String.valueOf(currentStoreId));
        if (CollectionUtils.isEmpty(currentItemCatList)){
            return "当前店铺分类列表为空，复制操作失败";
        }
        Map<String,Long> currentItemCatMap = new HashMap<>();
        for (TbItemCat tbItemCat : currentItemCatList) {
            currentItemCatMap.put(tbItemCat.getName(),tbItemCat.getId());
        }

        //初始化店铺商品信息
        for (TbItemCat tbItemCat : targetItemCatList) {
            List<TbItem> goodsListByCategoryId = tbItemMapper.getGoodsListByCategoryId(tbItemCat.getId());
            if (CollectionUtils.isEmpty(goodsListByCategoryId)){
                log.info("current category goods info is null");
                continue;
            }
            Long currentItemCatId = currentItemCatMap.get(tbItemCat.getName());
            for (TbItem tbItem : goodsListByCategoryId) {
                tbItem.setStoreId(currentStoreId);
                tbItem.setCid(currentItemCatId);
                tbItem.setCreated(new Date());
                tbItem.setUpdated(new Date());
                tbItemMapper.addGoods(tbItem);
            }
        }
        //初始化店铺库存信息
        initStoreGoods(currentStoreId);
        return CommonConstant.SUCCESS;
    }

    public void initStoreGoods(Integer storeId ){

        if (storeId == null || storeId == 0){
            return ;
        }
        try {
            TbStore store = tbStoreMapper.selectByPrimaryKey(storeId.intValue());
            tbStoreGoodsMapper.deleteByStoreId(storeId.intValue());
            List<TbItem> goodsList = tbItemMapper.findGoodsByStoreId(storeId.intValue());
            if (CollectionUtils.isEmpty(goodsList)){
                log.info("initStoreGoods goodsList is null");
                return ;
            }
            log.info("initStoreGoods goodsList size : {}",goodsList.size());
            Date currentDate = new Date();

            List<TbStoreGoods> storeGoodsList = buildStoreGoodsInventory(store, goodsList, currentDate);
            tbStoreGoodsMapper.insertBatch(storeGoodsList);
        }catch (Exception e){
            log.error("getGoodsDetailById happen exception ",e);
        }
    }

    public List<TbStoreGoods> buildStoreGoodsInventory( TbStore store, List<TbItem> goodsList, Date currentDate) {
        List<TbStoreGoods> storeGoodsList = new ArrayList<>();
        for (TbItem tbItem : goodsList) {
            TbStoreGoods tbStoreGoods = new TbStoreGoods();
            tbStoreGoods.setGoodsId(tbItem.getId().intValue());
            tbStoreGoods.setGoodsName(tbItem.getTitle());
            tbStoreGoods.setCreateTime(currentDate);
            tbStoreGoods.setUpdateTime(currentDate);
            tbStoreGoods.setStoreId(store.getId());
            tbStoreGoods.setStoreName(store.getStoreName());
            tbStoreGoods.setGoodsInventory(10);
            storeGoodsList.add(tbStoreGoods);
        }
        return storeGoodsList;
    }

    public List<TbItem> findGoodsByStoreId(Integer storeId) {
        return tbItemMapper.findGoodsByStoreId(storeId);
    }
}
