package com.ibay.tea.cms.controller.user;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.cms.service.user.CmsUserService;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbCmsUser;
import com.ibay.tea.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/user")
public class CmsUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserController.class);

    @Resource
    private CmsUserService cmsUserService;

    @Resource
    private ApiUserService apiUserService;

    @RequestMapping("/detail")
    public ResultInfo findUserById(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	TbCmsUser tbCmsUser = cmsUserService.findUserById(id);
        	resultInfo.setData(tbCmsUser);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	List<TbCmsUser> userList = cmsUserService.findAll();
        	resultInfo.setData(userList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addCmsUser(@RequestBody TbCmsUser tbCmsUser){

        if (tbCmsUser == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsUserService.addCmsUser(tbCmsUser);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteCmsUser(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsUserService.deleteCmsUser(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateCmsUser(@RequestBody TbCmsUser tbCmsUser){

        if (tbCmsUser == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsUserService.updateCmsUser(tbCmsUser);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }
    }

    @PostMapping("/apiUser/list")
    @ResponseBody
    public ResultInfo apiUserListByPage(@RequestBody Map<String,String> params){
        try {
            if (CollectionUtils.isEmpty(params)){
                return ResultInfo.newEmptyParamsResultInfo();
            }

            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
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
            LOGGER.error("cms goods list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/apiUser/bindCompany")
    public ResultInfo bindCompany(@RequestBody Map<String,Integer> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            boolean flag = apiUserService.bindCompany(params);
            if (flag){
                return resultInfo;
            }else {
                return ResultInfo.newFailResultInfo();
            }
        }catch (Exception e){
            LOGGER.error("apiUser bindCompany happen exception ",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
