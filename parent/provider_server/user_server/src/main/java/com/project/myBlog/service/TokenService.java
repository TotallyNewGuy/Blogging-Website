package com.project.myBlog.service;

import com.project.myBlog.dao.pojo.SysUser;

public interface TokenService {
    SysUser checkToken(String token);
}