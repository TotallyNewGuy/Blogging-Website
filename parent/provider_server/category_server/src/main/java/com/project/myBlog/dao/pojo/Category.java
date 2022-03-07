package com.project.myBlog.dao.pojo;

import lombok.Data;

// Category for articles
@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}