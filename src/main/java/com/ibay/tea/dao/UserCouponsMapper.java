package com.ibay.tea.dao;

import com.ibay.tea.entity.UserCoupons;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCoupons record);

    UserCoupons selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(UserCoupons record);
}