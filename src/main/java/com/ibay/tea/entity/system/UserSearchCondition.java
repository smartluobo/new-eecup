package com.ibay.tea.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户查询条件实体类
 */
@Data
public class UserSearchCondition {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 条件结果
     */
    private String conditionValue;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updated;
    /**
     * 数据类型：0测试 1运营 2公共
     */
    private String dataType;
}
