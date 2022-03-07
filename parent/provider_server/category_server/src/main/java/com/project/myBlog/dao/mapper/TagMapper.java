package com.project.myBlog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.myBlog.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    // query article tags by id
    List<Tag> findTagsByArticleId(Long articleId);

    // query top `limit` hot tags
    List<Long> findTopHotTagsByLimit(int limit);

    // query list of tag names by tag ids
    List<Tag> finTagsByTagId(List<Long> tagIds);
}
