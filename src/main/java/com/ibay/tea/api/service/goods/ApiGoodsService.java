package com.ibay.tea.api.service.goods;

import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TodayActivityBean;

import java.util.List;

public interface ApiGoodsService {

    List<TbItem> getGoodsListByCategoryId(long categoryId);

    TbItem getGoodsDetailById(long goodsId);

    void calculateGoodsPrice(List<TbItem> goodsListByCategoryId, int extraPrice, TodayActivityBean todayActivityBean);

    void checkGoodsInventory(List<TbItem> goodsList, int integer);

    void checkGoodsInventory(TbItem goodsInfo, int integer);

    void calculateGoodsPrice(TbItem goodsInfo, int extraPrice, TodayActivityBean todayActivityBean);
}
