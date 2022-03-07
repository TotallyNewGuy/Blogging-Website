package com.project.myBlog.controller;

import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.service.LogService;
import com.project.myBlog.service.TokenService;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.params.LogParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public Result login(@RequestBody LogParam logParam) {
        return logService.login(logParam);
    }

    // get header parameter
    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token){
        return logService.logout(token);
    }

    @PostMapping("register")
    public Result register(@RequestBody LogParam logParam) {
        // sso login
        System.out.println(logParam);
        return logService.register(logParam);
    }

    @GetMapping("check/{token}")
    public SysUser checkToken(@PathVariable("token") String token) {
        return tokenService.checkToken(token);
    }
}
