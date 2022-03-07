package com.project.myBlog.feignClient;

import com.project.myBlog.config.CustomLoadBalancerConfiguration;
import com.project.myBlog.vo.CategoryVo;
import com.project.myBlog.vo.TagVo;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "category-service")
// LoadBalancerClient is an annotation to set random algorithm
@LoadBalancerClient(name = "category-service", configuration = CustomLoadBalancerConfiguration.class)

public interface FromCategoryService {

    // full url(include categories)
    @GetMapping(value = "categorys/{id}")
    CategoryVo categoryById(@PathVariable(value = "id") Long id);
    // value cannot ignore

    // full url(include categories)
    @GetMapping("tags/{id}")
    List<TagVo> tagsByArticleId(@PathVariable(value = "id") Long id);
    // value cannot be ignored

    @GetMapping("tags/test")
    String testTag();
}
