package com.ibay.tea.dao;

import com.ibay.tea.entity.TbPrinter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbPrinterMapper {
    int insert(TbPrinter record);

    TbPrinter findById(@Param("printerId") int printerId);

    List<TbPrinter> findAll();

    void deletePrinter(int id);

    void saveUpdatePrinter(TbPrinter printer);
}