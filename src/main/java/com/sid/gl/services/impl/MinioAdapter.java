package com.sid.gl.services.impl;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;


@Service
public class MinioAdapter {

    @Autowired
    AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    String bucketName;

    @Value("${cloud.aws.s3.anonymous-files.enabled}")
    Boolean anonymousFilesEnabled;


    @SneakyThrows
    public String uploadFile(MultipartFile file){
        String keyName = getKeyName(Objects.requireNonNull(file.getOriginalFilename()),anonymousFilesEnabled);
        s3Client.putObject(
               bucketName,
               keyName,
               new ByteArrayInputStream(file.getBytes()),
               buildObjectMetadata(file)

       );
       return keyName;
    }

    @SneakyThrows
    public byte[] getObject(@NonNull String objectId){
       final S3Object s3Object=
       s3Client.getObject(bucketName,objectId);
       return s3Object.getObjectContent().readAllBytes();
    }

    static ObjectMetadata buildObjectMetadata(@NonNull MultipartFile file){
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.addUserMetadata("filename",file.getOriginalFilename());
        return objectMetadata;
    }


    static String getKeyName(@NonNull String fileName, Boolean anonymousFilesEnabled) {
        final String[] fix = fileName.split("\\.");
        String identifiant = UUID.randomUUID().toString();
        return (fix.length < 2 || anonymousFilesEnabled)
                ? identifiant
                : (fix[0] + "_" + identifiant + "." + fix[1]);
    }

}
