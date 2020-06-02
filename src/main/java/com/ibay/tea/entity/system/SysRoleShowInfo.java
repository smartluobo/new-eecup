package com.ibay.tea.entity.system;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统菜单展示信息
 */
@Data
public class SysRoleShowInfo {
    /**
     * 菜单树
     */
    private SysMenuTree sysMenuTree;
    /**
     * 角色类型
     */
    private Map<Integer, String> roleType;
    /**
     * 数据权限
     */
    private Map<Integer, List<SysRole>> dataAuthor;
    /**
     * tab权限
     */
    private Map<Integer, Set<String>> tabAuthor;
}
