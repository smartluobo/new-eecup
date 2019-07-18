package com.ibay.tea.api.controller.user;

import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/user")
public class ApiUserController {

    @Resource
    private ApiUserService apiUserService;


    public User findUserById(){
        return null;
    }
}
