package com.ibay.tea.dao;

import com.ibay.tea.entity.TbSkuDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbSkuDetailMapper {
    int deleteByPrimaryKey(Integer id);

    TbSkuDetail selectByPrimaryKey(Integer id);

    List<TbSkuDetail> findAll();

    void addSkuDetail(TbSkuDetail tbSkuDetail);

    void saveUpdateSkuDetail(TbSkuDetail tbSkuDetail);

    List<TbSkuDetail> findSkuDetailByTypeId(@Param("typeId") int typeId);
}