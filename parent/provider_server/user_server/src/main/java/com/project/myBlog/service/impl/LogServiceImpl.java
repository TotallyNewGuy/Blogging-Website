package com.project.myBlog.service.impl;

import com.alibaba.fastjson.JSON;
import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.service.LogService;
import com.project.myBlog.service.SysUserService;
import com.project.myBlog.utils.JWTUtils;
import com.project.myBlog.vo.ErrorCode;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.params.LogParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "myBlog!@#";

    @Override
    public Result login(LogParam logParam) {
        // step 1 check if logParam is valid
        // step 2 query user table using username and password
        // if it doesn't exist, login fail
        // otherwise, use JWT generate token and return to frontend
        // step 3 put token into redis and set expire time
        // Then, check if token is valid amd check if there is a user in redis
        String account = logParam.getAccount();
        String password = logParam.getPassword();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        password = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account, password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    @Override
    public Result register(LogParam logParam) {
        /*
        * 1. if logParam is valid
        * 2. if username is existed
        * 3. register this username
        * 4. generate token
        * 5. put it into Redis and return
        * 6. if there is something wrong, go back
        * */

        String account = logParam.getAccount();
        String password = logParam.getPassword();
        String nickname = logParam.getNickName();

        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 -> true
        sysUser.setDeleted(0); // 0 -> false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);

        //token
        String token = JWTUtils.createToken(sysUser.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
