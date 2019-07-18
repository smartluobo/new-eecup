package com.ibay.tea.cms.controller.login;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.login.CmsLoginService;
import com.ibay.tea.entity.TbCmsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin()
@RequestMapping("cms/login")
public class CmsLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsLoginController.class);

    @Resource
    private CmsLoginService loginService;

    @PostMapping(value = "/login")
    @ResponseBody
    public ResultInfo login(HttpServletRequest request,@RequestBody TbCmsUser tbCmsUser){

        if (tbCmsUser == null){
            LOGGER.error("login user info is null");
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            LOGGER.error("login user info :{}",tbCmsUser);
            TbCmsUser dbUser = loginService.login(tbCmsUser);
            if (dbUser == null){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            //处理session
            request.getSession().setAttribute("user",dbUser);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            dbUser.setPassword("********");
            resultInfo.setData(dbUser);
            LOGGER.info("login success ....");
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms user login happen Exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @PostMapping(value = "/loginOut")
    @ResponseBody
    public ResultInfo login(HttpServletRequest request){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            request.getSession().removeAttribute("user");
            LOGGER.info("login success ....");
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms user login out happen Exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @PostMapping(value = "/isLogin")
    @ResponseBody
    public ResultInfo isLogin(HttpServletRequest request){
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null){
                for (Cookie cookie : cookies) {
                    LOGGER.info("cookie : {}",cookie);
                }
            }

            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            Object user = request.getSession().getAttribute("user");
            if (user == null){
                return ResultInfo.newNoLoginResultInfo();
            }else {
                resultInfo.setData(user);
                LOGGER.info("request ready login...");
                return resultInfo;
            }
        }catch (Exception e){
            LOGGER.error("cms user login out happen Exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @PostMapping(value = "/test")
    @ResponseBody
    public ResultInfo test(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData("hello world");
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
