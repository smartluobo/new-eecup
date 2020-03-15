package com.ibay.tea.api.controller.members;
import com.ibay.tea.api.service.members.ApiMembersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("api/members")
public class ApiMembersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiMembersController.class);

    @Resource
    private ApiMembersService apiMembersService;

    @RequestMapping("/bindEnterpriseVIP")
    public String findOrderByOppenId(@RequestBody Map<String,String> params){
        if (params == null){
            return "参数为空";
        }
        String oppenId = params.get("oppenId");
        LOGGER.info("bindEnterpriseVIP oppenId : {}",oppenId);
        String result = apiMembersService.bindEnterpriseVIP(oppenId);
        return result;
    }
}
