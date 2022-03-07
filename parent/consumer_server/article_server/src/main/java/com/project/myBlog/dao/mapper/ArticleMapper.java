package com.project.myBlog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myBlog.dao.dos.Archive;
import com.project.myBlog.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Archive> getAllArchive();

    IPage<Article> listArticle(Page<Article> page,
                                   Long categoryId,
                                   Long tagId,
                                   String year,
                                   String month);
}
