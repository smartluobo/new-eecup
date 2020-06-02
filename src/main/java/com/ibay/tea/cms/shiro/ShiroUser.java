package com.ibay.tea.cms.shiro;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 自定义用户对象
 */
@Data
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = 6888918608743606757L;
    /** 用户id **/
    private Integer id;
    /** 登录名称 **/
    private String loginName;
    /** 用户名称 **/
    private String name;

    public ShiroUser(Integer id, String loginName, String name) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
    }

    @Override
    public String toString() {
        return loginName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()){
            return false;
        }
        ShiroUser other = (ShiroUser) o;
        if (loginName == null) {
            if (other.loginName != null) {
                return false;
            }
        } else if (!loginName.equals(other.loginName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginName);
    }
}
