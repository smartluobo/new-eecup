package com.ibay.tea.cms.controller.apiUser;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.entity.TbApiUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/user")
@Slf4j
public class CmsApiUserController {

    @Resource
    private ApiUserService apiUserService;

    @RequestMapping("/apiUser/list")
    @ResponseBody
    public ResultInfo apiUserListByPage(@RequestBody Map<String,String> params){
        try {
            if (CollectionUtils.isEmpty(params)){
                return ResultInfo.newEmptyParamsResultInfo();
            }

            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            int pageNum = Integer.valueOf(params.get("pageNum"));
            int pageSize = Integer.valueOf(params.get("pageSize"));
            String userPhone = params.get("userPhone");
            String companyId = params.get("companyId");

            Map<String,Object> condition = new HashMap<>();
            if (StringUtils.isNotEmpty(companyId)){
                condition.put("companyId",companyId);
            }
            if (!StringUtils.isEmpty(userPhone) && !"null".equals(userPhone)){
                condition.put("userPhone",userPhone);
            }
            int startIndex = (pageNum-1) * pageSize;
            condition.put("startIndex",startIndex);
            condition.put("pageSize",pageSize);
            long total = apiUserService.countUserByCondition(condition);
            List<TbApiUser> apiUserList = apiUserService.findUserListByPage(condition);
            resultInfo.setTotal(total);
            resultInfo.setData(apiUserList);
            return resultInfo;
        }catch (Exception e){
            log.error("cms goods list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/apiUser/bindCompany")
    public ResultInfo bindCompany(@RequestBody Map<String,Integer> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            boolean flag = apiUserService.bindCompany(params);
            if (flag){
                return resultInfo;
            }else {
                return ResultInfo.newFailResultInfo();
            }
        }catch (Exception e){
            log.error("apiUser bindCompany happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/apiUser/userCharge")
    public ResultInfo userCharge(@RequestBody Map<String,Integer> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            boolean flag = apiUserService.userCharge(params);
            if (flag){
                return resultInfo;
            }else {
                return ResultInfo.newFailResultInfo();
            }
        }catch (Exception e){
            log.error("apiUser bindCompany happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
