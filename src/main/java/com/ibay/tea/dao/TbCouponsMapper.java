package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCoupons;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public interface TbCouponsMapper {

    TbCoupons selectByPrimaryKey(Integer id);

    List<TbCoupons> findAll();

    void addCoupons(TbCoupons tbCoupons);

    void saveUpdateCoupons(TbCoupons tbCoupons);

    void deleteCoupons(int id);

    List<TbCoupons> findFullReduceCoupons();

}