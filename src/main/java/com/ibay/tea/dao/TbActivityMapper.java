package com.ibay.tea.dao;

import com.ibay.tea.entity.TbActivity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbActivityMapper {

    int deleteByPrimaryKey(Integer id);

    TbActivity selectByPrimaryKey(Integer id);

    List<TbActivity> findAll();

    TbActivity findHolidayActivity(@Param("currentDate") String dateYyyyMMdd, @Param("storeId") int storeId);

   TbActivity findRegularActivity(@Param("currentDate")String dateYyyyMMdd,@Param("storeId")int storeId);

    TbActivity findFullActivity(@Param("currentDate")String dateYyyyMMdd,@Param("storeId")int storeId);

    void addActivity(TbActivity tbActivity);

    void saveUpdateActivity(TbActivity tbActivity);

}