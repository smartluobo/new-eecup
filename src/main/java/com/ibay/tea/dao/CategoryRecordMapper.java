package com.ibay.tea.dao;

import com.ibay.tea.entity.CategoryRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryRecord record);

    CategoryRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(CategoryRecord record);
}