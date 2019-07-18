package com.ibay.tea.cms.controller.user;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.user.CmsUserService;
import com.ibay.tea.entity.TbCmsUser;
import com.ibay.tea.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("cms/user")
public class CmsUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserController.class);

    @Resource
    private CmsUserService cmsUserService;

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
}
