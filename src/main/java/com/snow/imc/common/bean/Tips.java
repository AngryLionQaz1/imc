package com.snow.imc.common.bean;
public enum Tips {


    FAIL(0,"失败"),
    SUCCESS(1,"成功"),
    DISABLED_TOEK(2,"token过期"),
    AUTHOR_NO(3,"没有访问权限"),
    USER_NOT("用户信息不存在"),
    PASSWORD_FALSE("密码错误"),
    TYPE_FALSE("文件类型不支持"),
    PROJECT_HAD("项目信息已存在")

    ;


    public Integer code;
    public String msg;


    Tips(String msg) {
        this.msg = msg;
    }

    Tips(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
