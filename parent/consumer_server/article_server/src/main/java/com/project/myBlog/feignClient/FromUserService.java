package com.project.myBlog.feignClient;

import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.vo.Result;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@EnableDiscoveryClient
@FeignClient(value = "user-service")
public interface FromUserService {

    // for openFeign from article_service
    @GetMapping("users/{authorId}")
    SysUser findUser(@PathVariable("authorId") Long id);

    // for openFeign from article_service
    @GetMapping("users/vo/{authorId}")
    Result findUserVo(@PathVariable("authorId") Long id);

}
