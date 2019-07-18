package com.ibay.tea.api.controller.pay;

import com.ibay.tea.api.service.pay.ApiPayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/pay")
public class ApiPayController {

    @Resource
    private ApiPayService apiPayService;




}
