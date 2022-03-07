package com.project.myBlog.service.impl;

import com.alibaba.fastjson.JSON;
import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.service.TokenService;
import com.project.myBlog.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser checkToken(String token) {
        // step 1
        // check if token is null
        if (StringUtils.isBlank(token)) {
            return null;
        }
        // step 2
        // check if token is legal
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) {
            return null;
        }

        // step 3
        // check if token exist in Redis database
        String userJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJSON)) {
            return null;
        }

        // get right user
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);
        return sysUser;
    }
}
