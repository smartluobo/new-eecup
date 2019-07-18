package com.ibay.tea.dao;

import com.ibay.tea.entity.TbStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbStoreMapper {

    TbStore selectByPrimaryKey(Integer id);

    List<TbStore> findAll();

    void addStore(TbStore tbStore);

    void deleteStore(int id);

    void saveUpdateStore(TbStore tbStore);

    List<TbStore> findByIds(List<String> strings);

}