package com.ibay.tea.dao;

import com.ibay.tea.entity.TbSmsConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbSmsConfigMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TbSmsConfig record);

    TbSmsConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(TbSmsConfig record);

    List<TbSmsConfig> findAll();

}