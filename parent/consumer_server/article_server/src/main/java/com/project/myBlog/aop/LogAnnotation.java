package com.project.myBlog.aop;

import java.lang.annotation.*;

// TYPE means can use in class, METHOD means can use it in method
@Target({ElementType.METHOD})
//@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default  "";
    String operation() default "";
}