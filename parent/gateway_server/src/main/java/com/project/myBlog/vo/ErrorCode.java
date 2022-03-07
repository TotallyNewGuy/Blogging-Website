package com.project.myBlog.vo;

public enum  ErrorCode {

    PARAMS_ERROR(10001,"Wrong parameter"),
    ACCOUNT_PWD_NOT_EXIST(10002,"Username or password doesn't exist"),
    TOKEN_ERROR(10003, "Token is illegal"),
    NO_PERMISSION(70001,"No permission"),
    SESSION_TIME_OUT(90001,"session time out"),
    NO_LOGIN(90002,"尝试一下变没变"),
    ACCOUNT_EXIST(10004, "username is existed");

    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

