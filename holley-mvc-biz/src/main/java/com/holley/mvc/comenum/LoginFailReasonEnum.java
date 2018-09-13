package com.holley.mvc.comenum;

/**
 * 接口返回结果类型 <br>
 */
public enum LoginFailReasonEnum {
    USER_OR_PWD_ERROR(1, "用户或密码有误"), VALID_CODE_ERROR(2, "验证码有误");

    private final int    value;
    private final String text;

    LoginFailReasonEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static String getText(int value) {
        LoginFailReasonEnum task = getEnmuByValue(value);
        return task == null ? null : task.getText();
    }

    public Short getShortValue() {
        Integer obj = value;
        return obj.shortValue();
    }

    /**
     * 通过传入的值匹配枚举
     * 
     * @param value
     * @return
     */
    public static LoginFailReasonEnum getEnmuByValue(int value) {
        for (LoginFailReasonEnum record : LoginFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
