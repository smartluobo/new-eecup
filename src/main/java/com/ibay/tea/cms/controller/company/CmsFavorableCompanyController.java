package com.ibay.tea.cms.controller.company;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.company.CmsFavorableCompanyService;
import com.ibay.tea.entity.TbFavorableCompany;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/company")
@Slf4j
public class CmsFavorableCompanyController {


    @Resource
    private CmsFavorableCompanyService cmsFavorableCompanyService;

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<TbFavorableCompany> companyList = cmsFavorableCompanyService.findAll();
            resultInfo.setData(companyList);
            return resultInfo;
        }catch (Exception e){
            log.error("FavorableCompany list happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addFavorableCompany(@RequestBody TbFavorableCompany favorableCompany){
        if (favorableCompany == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            if (favorableCompany.getCouponsType() == 0){
                //优惠企业新增
                if (StringUtils.isEmpty(favorableCompany.getCompanyRatio())){
                    return ResultInfo.newFailResultInfo("优惠企业折扣率不能为空");
                }
                try {
                    double v = Double.parseDouble(favorableCompany.getCompanyRatio());
                    if (v <= 0){
                        return ResultInfo.newFailResultInfo("优惠企业折扣率不能小于等于0");
                    }
                }catch (Exception e){
                    return ResultInfo.newFailResultInfo("优惠企业折扣率只能为数字");
                }
            }
            cmsFavorableCompanyService.addFavorableCompany(favorableCompany);
            return resultInfo;
        }catch (Exception e){
            log.error("cms FavorableCompany add happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteFavorableCompany(@PathVariable("id") int id){

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsFavorableCompanyService.deleteFavorableCompany(id);
            return resultInfo;
        }catch (Exception e){
            log.error("cms FavorableCompany delete id : {} happen exception ",id,e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateFavorableCompany(@RequestBody TbFavorableCompany favorableCompany){

        if (favorableCompany == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            cmsFavorableCompanyService.updateFavorableCompany(favorableCompany);
            return resultInfo;
        }catch (Exception e){
            log.error("cms FavorableCompany update happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }


}
