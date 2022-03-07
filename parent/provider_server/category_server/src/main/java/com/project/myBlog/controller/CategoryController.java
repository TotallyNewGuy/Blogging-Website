package com.project.myBlog.controller;

import com.project.myBlog.service.CategoryService;
import com.project.myBlog.vo.CategoryVo;
import com.project.myBlog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categorys")
//@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    // query all categories
    public Result categories() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result categoriesDetail() {
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id) {
        return categoryService.findCategoryDetailById(id);
    }

    // for openFeign from article_service
    @GetMapping("{id}")
    public CategoryVo categoryById(@PathVariable("id") Long id) {
        return categoryService.findCategoryById(id);
    }
}
