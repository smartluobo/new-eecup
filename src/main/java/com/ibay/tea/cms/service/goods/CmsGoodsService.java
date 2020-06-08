package com.ibay.tea.cms.service.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.entity.TbItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CmsGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;
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
}
