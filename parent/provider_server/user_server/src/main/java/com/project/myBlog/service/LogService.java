package com.project.myBlog.service;

import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.params.LogParam;
import org.springframework.transaction.annotation.Transactional;

// affair
@Transactional
public interface LogService {

    Result login(LogParam logParam);

    Result logout(String token);

    // register for a new user
    Result register(LogParam logParam);
}
