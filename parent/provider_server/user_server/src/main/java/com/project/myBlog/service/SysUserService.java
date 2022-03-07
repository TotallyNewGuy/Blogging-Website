package com.project.myBlog.service;

import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.UserVo;

public interface SysUserService {

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    // for openFeign from article_service
    Result findUserById(Long id);

    // for openFeign from article_service
    Result findUserVoById(Long id);

    void save(SysUser sysUser);
}
