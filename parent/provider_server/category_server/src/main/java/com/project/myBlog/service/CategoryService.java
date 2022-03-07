package com.project.myBlog.service;

import com.project.myBlog.vo.CategoryVo;
import com.project.myBlog.vo.Result;

public interface CategoryService {
    // for openFeign from article_service
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result findCategoryDetailById(Long id);
}
