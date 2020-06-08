package com.ibay.tea.cms.controller.system;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/system")
@Slf4j
public class LoginController {

    @PostMapping("/login")
    public ResultInfo doLogin(@RequestParam String username, @RequestParam String password) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()){
            return new ResultInfo(ReturnCodeEnum.LOGIN_SUCCESS);
        } else {
            return new ResultInfo(ReturnCodeEnum.LOGIN_ERROR);
        }
    }

    /**
     * 登录成功接口
     * @return
     */
    @RequestMapping(value = "/login/success")
    public Object loginSuccess() {
        ResultInfo resultInfo = new ResultInfo(ReturnCodeEnum.LOGIN_SUCCESS);
        log.info("-----------login success!");
        return resultInfo;
    }


    @RequestMapping(value = "/login/error")
    public Object loginError() {
        ResultInfo resultInfo = new ResultInfo(ReturnCodeEnum.LOGIN_ERROR);
        log.info("-----------login error!");
        return resultInfo;
    }
}
