package com.example.fintrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3ServiceImpl {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3ServiceImpl(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String email) throws IOException {
        String key = email + "/receipts" + UUID.randomUUID() + "-"+ file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    public InputStream downloadFile(String key){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

}
