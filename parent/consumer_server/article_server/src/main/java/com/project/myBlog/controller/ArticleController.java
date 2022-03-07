package com.project.myBlog.controller;

import com.project.myBlog.service.ArticleService;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.params.ArticleParam;
import com.project.myBlog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// JSON data interact
@RestController
@RequestMapping(value = "articles", method = RequestMethod.GET)
//@CrossOrigin
public class ArticleController {

    // top number for display
    static int LIMIT = 5;

    @Autowired
    private ArticleService articleService;

    @PostMapping()
    public Result getArticles(@RequestBody PageParams pageParams) {
        Result r = articleService.getArticles(pageParams);
        System.out.println(r.getData().toString());
        return articleService.getArticles(pageParams);
    }

    // top 5 hot articles
    @PostMapping("hot")
    public Result getHotArticles() {
        return articleService.getHotArticles(LIMIT);
    }

    // top 5 new articles
    @PostMapping("new")
    public Result getLastArticles() {
        return articleService.getNewArticles(LIMIT);
    }

    // article archives
    // need to be changed
    @PostMapping("listArchives")
    public Result getArticleArchive() {
        return articleService.getArchive();
    }

    @PostMapping("view/{id}")
    public Result getArticleById(@PathVariable("id") Long articleId) throws Exception {
        return articleService.getArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }


    @PostMapping("test")
    public String test() {
        return articleService.innerTest();
    }
}
