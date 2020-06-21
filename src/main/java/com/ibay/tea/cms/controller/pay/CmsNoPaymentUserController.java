package com.ibay.tea.cms.controller.pay;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.pay.CmsNoPaymentUserService;
import com.ibay.tea.common.CommonConstant;
import com.ibay.tea.entity.TbNoPaymentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/noPaymentUser")
@Slf4j
public class CmsNoPaymentUserController {

    @Resource
    private CmsNoPaymentUserService cmsNoPaymentUserService;

    @RequestMapping("/list")
    public ResultInfo findNoPaymentUserByPage(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        return cmsNoPaymentUserService.findNoPaymentUserByPage(params);
    }

    @RequestMapping("/add")
    public ResultInfo addNoPaymentUserByPage(@RequestBody TbNoPaymentUser tbNoPaymentUser){
        if (tbNoPaymentUser == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            String result = cmsNoPaymentUserService.addNoPaymentUserByPage(tbNoPaymentUser);
            if (CommonConstant.SUCCESS.equals(result)){
                return ResultInfo.newCmsSuccessResultInfo();
            }
            return ResultInfo.newFailResultInfo(result);
        }catch (Exception e){
            log.error("addNoPaymentUserByPage happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/delete")
    public ResultInfo addNoPaymentUserByPage(int id){
        if (id == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            String result = cmsNoPaymentUserService.deleteNoPaymentUserByPage(id);
            if (CommonConstant.SUCCESS.equals(result)){
                return ResultInfo.newCmsSuccessResultInfo();
            }
            return ResultInfo.newFailResultInfo(result);
        }catch (Exception e){
            log.error("addNoPaymentUserByPage happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
