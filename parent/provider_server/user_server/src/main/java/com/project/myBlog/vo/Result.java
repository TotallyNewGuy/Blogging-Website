package com.project.myBlog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    // unify Json data format
    private boolean success;

    private int code;

    private String msg;

    private Object data;

    // process success
    public static Result success(Object data) {
        return new Result(true, 200, "success", data);
    }

    // process fail
    public static Result fail(int code, String msg) {
        return new Result(false, code, msg, null);
    }
}
