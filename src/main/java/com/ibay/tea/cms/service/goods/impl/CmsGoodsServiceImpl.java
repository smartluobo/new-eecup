package com.ibay.tea.cms.service.goods.impl;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.dao.GoodsMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.entity.Goods;
import com.ibay.tea.entity.TbItem;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CmsGoodsServiceImpl implements CmsGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;
    @Override
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

    @Override
    public long countGoodsByCondition(Map<String,Object> condition){
        return tbItemMapper.countGoodsByCondition(condition);
    }

    @Override
    public void updateGoods(TbItem tbItem) {

        TbItem dbGoods = tbItemMapper.selectByPrimaryKey(tbItem.getId());
        if (dbGoods == null){
            return;
        }
        tbItemMapper.deleteGoods(tbItem.getId());
        tbItemMapper.saveUpdateGoods(tbItem);

    }

    @Override
    public void deleteGoods(long id) {
        tbItemMapper.deleteGoods(id);
    }

    @Override
    public void addGoods(TbItem tbItem) {
        tbItemMapper.addGoods(tbItem);
    }
}
