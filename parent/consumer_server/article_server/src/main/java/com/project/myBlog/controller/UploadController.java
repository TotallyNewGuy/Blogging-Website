package com.project.myBlog.controller;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.project.myBlog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("upload")
//@CrossOrigin
public class UploadController {

    @Autowired
    private AmazonS3 s3;

    String bucket_name = "microservice-blog";
    String endpoint = "https://microservice-blog.s3.us-west-1.amazonaws.com/";

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file) {
        // if original file name is example.png
        // will get "example.png"
        String originalFilename = file.getOriginalFilename();
        // get unique file name
        String fileName = UUID.randomUUID().toString() + "." +
                StringUtils.substringAfterLast(originalFilename, ".");
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        // upload to picture server
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(fileSize);

        System.out.format("Uploading %s to S3 bucket %s...\n", fileName, bucket_name);
        try {
            s3.putObject(bucket_name, fileName, file.getInputStream(), objectMetadata);
            // download
            return Result.success(endpoint + fileName);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.fail(20001, "upload failed");
    }
}