package com.project.myBlog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.myBlog.dao.mapper.ArticleMapper;
import com.project.myBlog.feignClient.FromCategoryService;
import com.project.myBlog.feignClient.FromUserService;
import com.project.myBlog.service.ArticleService;
import com.project.myBlog.service.ThreadService;
import com.project.myBlog.vo.*;
import com.project.myBlog.vo.params.PageParams;
import com.project.myBlog.dao.dos.Archive;
import com.project.myBlog.dao.mapper.ArticleBodyMapper;
import com.project.myBlog.dao.mapper.ArticleTagMapper;
import com.project.myBlog.dao.pojo.Article;
import com.project.myBlog.dao.pojo.ArticleBody;
import com.project.myBlog.dao.pojo.ArticleTag;
import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.utils.UserThreadLocal;
import com.project.myBlog.vo.params.ArticleParam;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    // Error in IntelliJ but it runs successfully
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ThreadService threadService;

    // feign
    @Autowired
    private FromCategoryService fromCategoryService;

    // feign
    @Autowired
    private FromUserService fromUserService;


    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback1")
    @Retry(name = "backendA", fallbackMethod = "fallback1")
    public Result getArticles(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));
    }

    public Result fallback1(PageParams pageParams, Exception e) {
        return Result.fail(666, "Circuit breaker activated");
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback2")
    @Retry(name = "backendA", fallbackMethod = "fallback2")
    public Result getHotArticles(int limit) {
        // according to view count to get
        // specific ids and titles of articles
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);

        // use wrapper to replace sql to query in MySQL
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback2")
    @Retry(name = "backendA", fallbackMethod = "fallback2")
    public Result getNewArticles(int limit) {
        // according to create date, get
        // specific ids and titles of articles
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);

        // use wrapper to replace sql to query in MySQL
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        for (Article a: articleList) {
            System.out.println("bodyId is: " + a.getBodyId());
        }
        return Result.success(copyList(articleList, false, false));
    }

    public Result fallback2(int limit, Exception e) {
        return Result.fail(666, "Circuit breaker activated");
    }

    @Override
//    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback3")
//    @Retry(name = "backendA", fallbackMethod = "fallback3")
    public Result getArchive() {
        List<Archive> archiveList = articleMapper.getAllArchive();
        return Result.success(archiveList);
    }

    public Result fallback3(Exception e) {
        return Result.fail(666, "Circuit breaker activated");
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback4")
    @Retry(name = "backendA", fallbackMethod = "fallback4")
    public Result getArticleById(Long articleId) {
        /*
         * 1. query article by id
         * 2. query details by bodyId and category of this article and
         * */

        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        // at the same time, update view count for this article
        // asynchronous
        // put update operation into thread pool
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    public Result fallback4(Long articleId, Exception e) {
        return Result.fail(666, "Circuit breaker activated");
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback5")
    @Retry(name = "backendA", fallbackMethod = "fallback5")
    public Result publish(ArticleParam articleParam) {
        // if we want to use thread local to get user,
        // must intercept this request
        SysUser sysUser = UserThreadLocal.get();

        /**
         * 1. save article
         * 2. get author id
         * 3. set tags id
         * 4. set article body id
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        // generate id for article automatically
        articleMapper.insert(article);

        // tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());

        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());

        return Result.success(articleVo);
    }

    public Result fallback5(ArticleParam articleParam, Exception e) {
        return Result.fail(666, "Circuit breaker activated");
    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback6")
    @Retry(name = "backendA", fallbackMethod = "fallback6")
    private ArticleBodyVo getArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    public ArticleBodyVo fallback6(Long bodyId, Exception e) {
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent("Circuit breaker activated");
        return articleBodyVo;
    }

    // ================ a series of copy methods ================

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> res = new ArrayList<>();
        for (Article article : records) {
            res.add(copy(article, isTag, isAuthor,false, false));
        }
        return res;
    }

    private ArticleVo copy(Article article,
                           boolean isTag,
                           boolean isAuthor,
                           boolean isBody,
                           boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        // Long to String
        articleVo.setCreateDate(new DateTime(articleVo.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(fromCategoryService.tagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            SysUser sysUser = fromUserService.findUser(authorId);
            articleVo.setAuthor(sysUser.getNickname());
        }

        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(getArticleBodyById(bodyId));
        }

        if (isCategory) {
            Long categoryId = article.getCategoryId();
//            articleVo.setCategory(categoryService.findCategoryById(categoryId));

            // remote call api from category_service
            articleVo.setCategory(fromCategoryService.categoryById(categoryId));
        }

        return articleVo;
    }

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "testMethod")
    public String innerTest() {
        return fromCategoryService.testTag();
    }

    private String testMethod(Exception e) {
        System.out.println("错误是你: \n" + e);
        System.out.println("=====================");
        return "进入降级方法";
    }
}
