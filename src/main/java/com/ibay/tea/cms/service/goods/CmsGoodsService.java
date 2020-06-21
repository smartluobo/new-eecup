package com.ibay.tea.cms.service.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.dao.TbItemCatMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbItemCat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CmsGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;
    @Resource
    private TbItemCatMapper tbItemCatMapper;

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

    public String copyStoreGoods(Integer currentStoreId, Integer targetStoreId) {
        Long targetStoreGoodsCount = tbItemMapper.countGoodsByStoreId(currentStoreId);
        if (targetStoreGoodsCount != null || targetStoreGoodsCount > 0){
            return "目标店铺已经存在商品，不支持该功能";
        }

        List<TbItemCat> currentItemCatList = tbItemCatMapper.findByStoreId(String.valueOf(currentStoreId));
        if (CollectionUtils.isEmpty(currentItemCatList)){
            return "当店铺分类为空，不支持复制";
        }

        for (TbItemCat tbItemCat : currentItemCatList) {
            tbItemCat.setId(null);
            tbItemCat.setStoreId(String.valueOf(targetStoreId));
            tbItemCat.setCreated(new Date());
            tbItemCat.setUpdated(new Date());
            tbItemCatMapper.addCategory(tbItemCat);
        }
        List<TbItemCat> targetItemCatList = tbItemCatMapper.findByStoreId(String.valueOf(targetStoreId));
        Map<String,Long> targetItemCatMap = new HashMap<>();
        for (TbItemCat tbItemCat : targetItemCatList) {
            targetItemCatMap.put(tbItemCat.getName(),tbItemCat.getId());
        }

        for (TbItemCat tbItemCat : currentItemCatList) {
            List<TbItem> goodsListByCategoryId = tbItemMapper.getGoodsListByCategoryId(tbItemCat.getId());
            if (CollectionUtils.isEmpty(goodsListByCategoryId)){
                continue;
            }
            Long targetItemCatId = targetItemCatMap.get(tbItemCat.getName());
            for (TbItem tbItem : goodsListByCategoryId) {
                tbItem.setStoreId(targetStoreId);
                tbItem.setCid(targetItemCatId);
                tbItem.setCreated(new Date());
                tbItem.setUpdated(new Date());
                tbItemMapper.addGoods(tbItem);
            }
        }
        return CommonConstant.SUCCESS;
    }
}
