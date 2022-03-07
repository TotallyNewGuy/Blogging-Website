package com.project.myBlog.service;

import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.params.ArticleParam;
import com.project.myBlog.vo.params.PageParams;

public interface ArticleService {
    // get all articles
    Result getArticles(PageParams pageParams);

    // get top hot articles
    Result getHotArticles(int limit);

    // get top last articles
    Result getNewArticles(int limit);

    // articles archive
    Result getArchive();

    Result getArticleById(Long articleId);

    Result publish(ArticleParam articleParam);

    String innerTest();
}
