package com.ibay.tea.cms.shiro.interceptor;

import com.alibaba.fastjson.JSON;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.common.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Session拦截器
 */
@Slf4j
public class ShiroSessionFilter extends AccessControlFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        try {
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(JSON.toJSONString(new ResultInfo(ReturnCodeEnum.SESSION_EXPIRE)));
        } catch (IOException e) {
            log.error("write http servlet response error ", e);
        }
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        if (isLoginRequest(request, response) ) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            // If principal is not null, then the user is known and should be allowed access.
            return subject.getPrincipal() != null;
        }

    }
}
