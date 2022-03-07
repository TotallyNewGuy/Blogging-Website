package com.project.myBlog.service;

import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.params.CommentParam;

public interface CommentsService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
