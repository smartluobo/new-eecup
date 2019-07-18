package com.ibay.tea.cms.service.printer.impl;

import com.ibay.tea.cms.service.printer.CmsPrinterService;
import com.ibay.tea.dao.TbPrinterMapper;
import com.ibay.tea.entity.TbPrinter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsPrinterServiceImpl implements CmsPrinterService {

    @Resource
    private TbPrinterMapper tbPrinterMapper;


    @Override
    public List<TbPrinter> findAll() {
        return tbPrinterMapper.findAll();
    }

    @Override
    public void addPrinter(TbPrinter printer) {
        tbPrinterMapper.insert(printer);
    }

    @Override
    public void deletePrinter(int id) {
        tbPrinterMapper.deletePrinter(id);
    }

    @Override
    public void updatePrinter(TbPrinter printer) {
        TbPrinter dbPrinter = tbPrinterMapper.findById(printer.getId());
        if (dbPrinter == null){
            return ;
        }
        tbPrinterMapper.deletePrinter(printer.getId());
        tbPrinterMapper.saveUpdatePrinter(printer);
    }
}
