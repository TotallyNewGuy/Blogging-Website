package com.project.myBlog.service;

import com.project.myBlog.dao.pojo.Tag;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.TagVo;

import java.util.List;

public interface TagService {

    // for openFeign from article_service
    List<TagVo> findTagsByArticleId(Long articleId);

    Result getHotTags(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
