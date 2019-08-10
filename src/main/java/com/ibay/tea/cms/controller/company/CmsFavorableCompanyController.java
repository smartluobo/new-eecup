package com.ibay.tea.cms.controller.company;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.company.CmsFavorableCompanyService;
import com.ibay.tea.entity.TbFavorableCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/company")
public class CmsFavorableCompanyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsFavorableCompanyController.class);

    @Resource
    private CmsFavorableCompanyService cmsFavorableCompanyService;

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbFavorableCompany> companyList = cmsFavorableCompanyService.findAll();
            resultInfo.setData(companyList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("FavorableCompany list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addFavorableCompany(@RequestBody TbFavorableCompany printer){
        if (printer == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsFavorableCompanyService.addFavorableCompany(printer);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms FavorableCompany add happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteFavorableCompany(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsFavorableCompanyService.deleteFavorableCompany(id);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms FavorableCompany delete id : {} happen exception ",id,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateFavorableCompany(@RequestBody TbFavorableCompany favorableCompany){

        if (favorableCompany == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            cmsFavorableCompanyService.updateFavorableCompany(favorableCompany);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms FavorableCompany update happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }


}
