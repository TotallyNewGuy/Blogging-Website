package com.project.myBlog.controller;

import com.project.myBlog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
//@CrossOrigin
public class UploadController {

    @PostMapping
    public Result upload(@RequestParam("image")MultipartFile file) {
        // if original file name is example.png
        // will get "example.png"
        String originalFilename = file.getOriginalFilename();
        // get unique file name
        String fileName = UUID.randomUUID().toString() + "." +
                StringUtils.substringAfterLast(originalFilename, ".");

        // upload to picture server
        // need to be fixed

        return Result.success(null);
    }
}
