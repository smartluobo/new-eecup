package com.ibay.tea.dao;

import com.ibay.tea.entity.TbUserExperienceCoupons;

public interface TbUserExperienceCouponsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbUserExperienceCoupons record);

    int insertSelective(TbUserExperienceCoupons record);

    TbUserExperienceCoupons selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbUserExperienceCoupons record);

    int updateByPrimaryKey(TbUserExperienceCoupons record);
}