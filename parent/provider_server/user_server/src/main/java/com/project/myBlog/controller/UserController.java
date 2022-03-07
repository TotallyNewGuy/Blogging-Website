package com.project.myBlog.controller;

import com.project.myBlog.service.SysUserService;
import com.project.myBlog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
//@CrossOrigin
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    // /users/currentUser
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        // query user by token
        return sysUserService.findUserByToken(token);
    }

    // for openFeign from article_service
    @GetMapping("{authorId}")
    public Result findUser(@PathVariable("authorId") Long authorId) {
        // query user by token
        return sysUserService.findUserById(authorId);
    }

    // for openFeign from article_service
    @GetMapping("/vo/{authorId}")
    public Result findUserVo(@PathVariable("authorId") Long authorId) {
        // query user by token
        return sysUserService.findUserVoById(authorId);
    }
}
