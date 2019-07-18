package com.ibay.tea.dao;

import com.ibay.tea.entity.GoodsSkuType;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSkuTypeMapper {

    int insert(GoodsSkuType record);

}