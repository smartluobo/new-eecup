package com.ibay.tea.dao;

import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbApiUserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbApiUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbApiUser record);

    TbApiUser selectByPrimaryKey(Integer id);

    TbApiUser findApiUserByOppenId(String oppenId);

}