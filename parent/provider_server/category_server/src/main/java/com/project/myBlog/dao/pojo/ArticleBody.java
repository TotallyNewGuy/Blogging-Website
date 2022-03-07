package com.project.myBlog.dao.pojo;

import lombok.Data;
// content
@Data
public class ArticleBody {

    private Long id;
    // markDown
    private String content;
    // HTML
    private String contentHtml;
    private Long articleId;
}