package com.project.myBlog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.myBlog.dao.mapper.ArticleMapper;
import com.project.myBlog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        Integer viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();

        articleUpdate.setViewCounts(viewCounts + 1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        // find article by ID
        updateWrapper.eq(Article::getId, article.getId());
        // make sure this viewCount has not modified by other thread
        updateWrapper.eq(Article::getViewCounts, viewCounts);
        // update
        articleMapper.update(articleUpdate, updateWrapper);
        try {
            Thread.sleep(5000);
            System.out.println("view count update finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
