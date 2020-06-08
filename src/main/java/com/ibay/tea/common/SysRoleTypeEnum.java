package com.ibay.tea.common;

/**
 * 系统角色类型枚举类
 */
public enum SysRoleTypeEnum {
    /**
     * 测试
     **/
    TEST(0, "测试"),
    /**
     * 运营
     **/
    OPERATE(1, "运营");

    private int value;

    private String desc;

    SysRoleTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取对应的值
     * @param value
     * @return
     */
    public static String getByValue(Integer value) {
        for (SysRoleTypeEnum roleTypeEnum : SysRoleTypeEnum.values()) {
            if (value.equals(roleTypeEnum.getValue())) {
                return roleTypeEnum.getDesc();
            }
        }
        return null;
    }

}
