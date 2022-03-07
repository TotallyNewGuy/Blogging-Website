package com.project.myBlog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.myBlog.dao.mapper.TagMapper;
import com.project.myBlog.dao.pojo.Tag;
import com.project.myBlog.service.TagService;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    // for feign
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result getHotTags(int limit) {
        // sort tag_id by the frequency of tags articles contains
        // step 1. count tags frequency
        // step 2. sort as frequency
        List<Long> tagIds = tagMapper.findTopHotTagsByLimit(limit);

        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }

        // get all tag names by tag ids
        List<Tag> tagNames = tagMapper.finTagsByTagId(tagIds);
        return Result.success(tagNames);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tags = tagMapper.selectList(objectLambdaQueryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = tagMapper.selectList(objectLambdaQueryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        System.out.println("服务层id: " + id);
        Tag tag = tagMapper.selectById(id);
        System.out.println("是不是true: " + (tag == null));
        System.out.println("tag name: " + tag.getTagName());
        return Result.success(copy(tag));
    }


    // ================ a series of copy methods ================

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }
}
