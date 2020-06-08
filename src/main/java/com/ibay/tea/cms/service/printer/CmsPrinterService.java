package com.ibay.tea.cms.service.printer;

import com.ibay.tea.dao.TbPrinterMapper;
import com.ibay.tea.entity.TbPrinter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsPrinterService {

    @Resource
    private TbPrinterMapper tbPrinterMapper;


    public List<TbPrinter> findAll() {
        return tbPrinterMapper.findAll();
    }

    public void addPrinter(TbPrinter printer) {
        tbPrinterMapper.insert(printer);
    }

    public void deletePrinter(int id) {
        tbPrinterMapper.deletePrinter(id);
    }

    public void updatePrinter(TbPrinter printer) {
        TbPrinter dbPrinter = tbPrinterMapper.findById(printer.getId());
        if (dbPrinter == null){
            return ;
        }
        tbPrinterMapper.deletePrinter(printer.getId());
        tbPrinterMapper.saveUpdatePrinter(printer);
    }
}
