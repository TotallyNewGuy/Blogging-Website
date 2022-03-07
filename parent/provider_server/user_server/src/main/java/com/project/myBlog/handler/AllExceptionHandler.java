package com.project.myBlog.handler;

import com.project.myBlog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// intercept and handle all class has controller annotations
@ControllerAdvice
public class AllExceptionHandler {

    // handler all exceptions
    @ExceptionHandler(Exception.class)
    @ResponseBody // return Json data
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999, "Exception handler activated");
    }
}
