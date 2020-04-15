package com.ibay.tea.cms.service.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.entity.TbItem;

import java.util.List;
import java.util.Map;

public interface CmsGoodsService {
    ResultInfo findGoodsListByPage(Map<String,Object> condition, Integer pageNum, Integer pageSize);

    long countGoodsByCondition(Map<String,Object> condition);

    void updateGoods(TbItem tbItem);

    void deleteGoods(long id);

    void addGoods(TbItem tbItem);
}
