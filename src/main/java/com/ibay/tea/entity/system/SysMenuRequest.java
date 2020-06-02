package com.ibay.tea.entity.system;

import lombok.Data;

/**
 * 系统菜单请求保存实体

 */
@Data
public class SysMenuRequest extends SysMenu {
    /**
     * 父级菜单
     */
    private SysMenu parent;
}
