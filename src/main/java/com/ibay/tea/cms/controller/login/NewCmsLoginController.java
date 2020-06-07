package com.ibay.tea.cms.controller.login;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.login.NewCmsLoginService;
import com.ibay.tea.cms.service.system.SysUserService;
import com.ibay.tea.common.ReturnCodeEnum;
import com.ibay.tea.entity.system.SysUser;
import com.ibay.tea.entity.system.UpdatePasswordInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin()
@Slf4j
@RequestMapping("/cms/newLogin")
public class NewCmsLoginController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private NewCmsLoginService newCmsLoginService;

    /**
     * 登录接口
     * @return
     */
    @RequestMapping(value = "/system/login")
    public Object login(HttpServletRequest request,String username, String password) {
        if(StringUtils.isEmpty(username) && StringUtils.isEmpty(password)){
            return new ResultInfo(ReturnCodeEnum.LOGIN_ERROR);
        }
        log.info("login username:{}, password:{}", username, password);
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()){
            return new ResultInfo(ReturnCodeEnum.LOGIN_SUCCESS);
        } else {
            return new ResultInfo(ReturnCodeEnum.LOGIN_ERROR);
        }
    }



    /**
     * 退出接口
     * @return
     */
    @RequestMapping(value = "/system/logout", method = RequestMethod.GET)
    public Object logout() {
        ResultInfo resultInfo = new ResultInfo();
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();

        return resultInfo;
    }

    /**
     * 登录成功接口
     * @return
     */
    @RequestMapping(value = "/system/login/success")
    public Object loginSuccess() {
        ResultInfo resultInfo = new ResultInfo(ReturnCodeEnum.LOGIN_SUCCESS);
        log.info("-----------login success!");
        return resultInfo;
    }

    @RequestMapping(value = "/system/login/error")
    public Object loginError() {
        ResultInfo resultInfo = new ResultInfo(ReturnCodeEnum.LOGIN_ERROR);
        log.info("-----------login error!");
        return resultInfo;
    }


    /**
     * 获取图片验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/system/getCaptcha")
    public void getCaptchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("image/jpeg");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(newCmsLoginService.getCaptcha(request), "jpg", out);
            out.flush();
        } catch (IOException e) {
            log.error("Captcha generate error", e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 找回密码
     * @param username
     * @param email
     * @return
     */
    @RequestMapping(value = "/system/findPassword", method = RequestMethod.POST)
    public Object findPassword(String username, String email){
        ResultInfo resultInfo = new ResultInfo();
        SysUser sysUser = sysUserService.getUserByLoginName(username);
        if (null == sysUser) {
            return new ResultInfo(ReturnCodeEnum.USER_NOT_EXITS);
        }
        if (!sysUser.getEmail().equals(email)){
            return new ResultInfo(ReturnCodeEnum.USER_EMAIL_NOT_MATCH);
        }
        return resultInfo;
    }

    /**
     * 密码重置接口
     * @param userName
     * @param checkCode
     * @return
     */
    @RequestMapping(value = "/system/resetPassword", method = RequestMethod.GET)
    public Object resetPassword(@RequestParam(value = "username") String userName,
                                @RequestParam(value = "checkCode") String checkCode){
        ResultInfo resultInfo = new ResultInfo();
        boolean flag = false;
        if (!StringUtils.isEmpty(userName)) {
            SysUser sysUser = sysUserService.getUserByLoginName(userName);
            if (null != sysUser) {
                flag = newCmsLoginService.checkCodeValid(sysUser, checkCode);
            }
        }
        resultInfo.setData(flag);
        return resultInfo;
    }

    /**
     * 更新密码接口
     * @param updatePasswordInfo
     * @return
     */
    @RequestMapping(value = "/system/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(@RequestBody UpdatePasswordInfo updatePasswordInfo){
        ResultInfo resultInfo = new ResultInfo();
        SysUser sysUser = sysUserService.getUserByLoginName(updatePasswordInfo.getUsername());
        if (null == sysUser) {
            return new ResultInfo(ReturnCodeEnum.USER_NOT_EXITS);
        }
        if (!sysUserService.checkPassword(updatePasswordInfo.getOldPassword(), sysUser)) {
            return new ResultInfo(ReturnCodeEnum.OLD_PASSWORD_IS_ERROR);
        }
        newCmsLoginService.updatePassword(sysUser, updatePasswordInfo.getNewPassword());
        return resultInfo;
    }

}
