package com.ibay.tea.cms.controller.printer;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.printer.CmsPrinterService;
import com.ibay.tea.entity.TbPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/printer")
public class CmsPrinterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsPrinterController.class);

    @Resource
    private CmsPrinterService cmsPrinterService;

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbPrinter> menuList = cmsPrinterService.findAll();
            resultInfo.setData(menuList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addPrinter(@RequestBody TbPrinter printer){
        if (printer == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsPrinterService.addPrinter(printer);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms printer add happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deletePrinter(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsPrinterService.deletePrinter(id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms printer delete id : {} happen exception ",id,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updatePrinter(@RequestBody TbPrinter printer){

        if (printer == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsPrinterService.updatePrinter(printer);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms printer update happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }
}
