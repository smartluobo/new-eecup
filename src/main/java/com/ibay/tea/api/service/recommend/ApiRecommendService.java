package com.ibay.tea.api.service.recommend;

import com.ibay.tea.entity.TbItem;

import java.util.List;

public interface ApiRecommendService {

    List<TbItem> findRecommendList(String storeId);
}
