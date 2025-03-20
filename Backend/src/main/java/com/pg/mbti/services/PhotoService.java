package com.pg.mbti.services;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class PhotoService {
    private final MinioClient minioClient;
    private final String bucketName;

    public PhotoService(MinioClient minioClient, @Value("${minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public String uploadPhoto(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Ensure bucket exists
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Upload file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    public Resource getPhoto(String fileName) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file: " + e.getMessage());
        }
    }

    public ResponseEntity<Resource> getProfilePhotoResponse(String fileName) {
        try {
            if (fileName == null || fileName.equalsIgnoreCase("default.jpg")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(getPhoto(fileName));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
