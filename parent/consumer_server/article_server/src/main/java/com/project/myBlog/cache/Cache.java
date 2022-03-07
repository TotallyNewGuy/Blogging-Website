package com.project.myBlog.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache { // cut point

    // expire time
    long expire() default 1 * 60 * 1000;

    // key for cache
    String name() default "";

}