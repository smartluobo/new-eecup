package com.ibay.tea.cms.service.login;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ibay.tea.cms.service.system.SysUserService;
import com.ibay.tea.cms.shiro.interceptor.SystemShiroFilter;
import com.ibay.tea.common.utils.SHAUtil;
import com.ibay.tea.entity.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * 登录Service层
 */
@Service
@Slf4j
public class NewCmsLoginService {
    /** 半小时的毫秒数 **/
    private static final long HALF_HOUR = 30 * 60 * 1000L;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 获取图片验证码的流
     * @return
     */
    public BufferedImage getCaptcha(HttpServletRequest request){
        String text = defaultKaptcha.createText();
        log.info("Captcha generate:{}", text);
        request.getSession().setAttribute(SystemShiroFilter.DEFAULT_CAPTCHA_PARAM, text);
        BufferedImage bi = defaultKaptcha.createImage(text);
        return bi;
    }




    /**
     * 解密校验code
     * @param sysUser
     * @param checkCode
     * @return
     */
    private String decryptCheckCode(SysUser sysUser, String checkCode){
        return SHAUtil.decrypt(checkCode, sysUser.getSalt());
    }

    /**
     * 校验checkout是否正常
     * @param sysUser
     * @param checkCode
     * @return
     */
    public boolean checkCodeValid(SysUser sysUser, String checkCode){
        String code = decryptCheckCode(sysUser, checkCode);
        if (StringUtils.isBlank(code)){
            return false;
        }
        String[] array = checkCode.split(";");
        if (array.length != 3) {
            return false;
        }
        if (!StringUtils.equals(sysUser.getLoginName(), array[0])) {
            return false;
        }
        if (!StringUtils.equals(sysUser.getEmail(), array[1])) {
            return false;
        }
        // 检查是否在30分钟以内
        long now = System.currentTimeMillis();
        long old = Long.parseLong(array[2]);
        if ((now - old) > HALF_HOUR) {
            return false;
        }
        return true;
    }

    /**
     * 更新用户
     * @param user
     * @param newPassword
     */
    public void updatePassword(SysUser user, String newPassword){
        if (StringUtils.isNotBlank(newPassword)) {
            sysUserService.encryptPassword(user, newPassword);
        }
        sysUserService.updateSysUser(user);
    }


}
