package com.project.myBlog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.myBlog.dao.mapper.CommentsMapper;
import com.project.myBlog.dao.pojo.Comment;
import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.feignClient.FromUserService;
import com.project.myBlog.service.CommentsService;
import com.project.myBlog.utils.UserThreadLocal;
import com.project.myBlog.vo.CommentVo;
import com.project.myBlog.vo.Result;
import com.project.myBlog.vo.UserVo;
import com.project.myBlog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    // feign
    private FromUserService fromUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /*
        * 1. find article by id
        * 2. find author details by article
        * 3. if level == 1, get its children comments by parent id
        * */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, id);
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }

        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentsMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> findChildCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);

        return copyList(comments);
    }

    // ================= copy methods =================

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);

        Long authorId = comment.getAuthorId();
        UserVo userVo = (UserVo)fromUserService.findUserVo(authorId).getData();
        commentVo.setAuthor(userVo);

        Integer level = comment.getLevel();
        if (level == 1) {
            // no children comment
            Long id = comment.getId();
            // add children comment to it
            List<CommentVo> commentVoList = findChildCommentByParentId(id);
            commentVo.setChildren(commentVoList);
        }

        if (level > 1) {
            // children comment
            Long toUserId = comment.getToUid();
            UserVo toUserVo = (UserVo)fromUserService.findUserVo(toUserId).getData();
            commentVo.setToUser(toUserVo);
        }

        return commentVo;
    }


}
