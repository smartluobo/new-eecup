package com.ibay.tea.dao;

import com.ibay.tea.entity.TbRecommend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbRecommendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbRecommend record);

    TbRecommend selectByPrimaryKey(Integer id);

    List<Long> findStoreRecommend(String storeId);
}