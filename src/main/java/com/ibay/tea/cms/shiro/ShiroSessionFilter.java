package com.ibay.tea.cms.shiro;


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
 *
 * 从名字中我们也可以理解，他的逻辑是这样：先调用isAccessAllowed，如果返回的是true，则直接放行执行后面的filter和servlet，
 * 如果返回的是false，则继续执行后面的onAccessDenied方法，如果后面返回的是true则也可以有权限继续执行后面的filter和servelt。
 * 只有两个函数都返回false才会阻止后面的filter和servlet的执行。
 */
@Slf4j
public class ShiroSessionFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {

        if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            return subject.getPrincipal() != null;
        }
    }

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
}
