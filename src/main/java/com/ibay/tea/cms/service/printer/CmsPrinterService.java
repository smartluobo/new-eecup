package com.ibay.tea.cms.service.printer;

import com.ibay.tea.entity.TbPrinter;

import java.util.List;

public interface CmsPrinterService {
    List<TbPrinter> findAll();

    void addPrinter(TbPrinter printer);

    void deletePrinter(int id);

    void updatePrinter(TbPrinter printer);
}
