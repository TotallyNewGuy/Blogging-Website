package com.project.myBlog.controller;

import com.project.myBlog.service.TagService;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tags")
//@CrossOrigin
public class TagsController {

    static int LIMIT = 5;

    @Autowired
    private TagService tagService;

    @GetMapping("hot")
    public Result getTopHotTags() {
        // top 5 hot tags
        return tagService.getHotTags(LIMIT);
    }

    @GetMapping
    public Result findAll() {
        // top 5 hot tags
        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result getAllDetail() {
        // top 5 hot tags
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result getDetailById(@PathVariable("id") Long id) {
        return tagService.findDetailById(id);
    }

    // for openFeign from article_service
    @GetMapping("{id}")
    public List<TagVo> tagsByArticleId(@PathVariable("id") Long id) {
        // List of tag vo
        List<TagVo> list = tagService.findTagsByArticleId(id);
        return list;
    }

    @GetMapping("test")
    public String testTag() {
        return "test is successful";
    }
}
