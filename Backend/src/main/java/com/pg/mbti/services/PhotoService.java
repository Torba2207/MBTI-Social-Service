package com.pg.mbti.services;

import com.pg.mbti.configurations.MinioConfig;
import com.pg.mbti.exceptions.FileNotFoundException;
import com.pg.mbti.exceptions.FileUploadException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final MinioClient minioClient;

    @Value("${image.default.path}")
    private String defaultImagePath;

    public String uploadPhoto(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                throw new FileUploadException("Invalid file name");
            }

            String fileName = originalFilename.equals(defaultImagePath)
                    ? originalFilename
                    : UUID.randomUUID() + "_" + originalFilename;

            ensureBucketExists();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinioConfig.MinioProperties.bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            throw new FileUploadException(String.format("Failed to upload file: %s", file.getOriginalFilename()));
        }
    }

    public Resource getPhoto(String fileName) {
        try {
            return new InputStreamResource(minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(MinioConfig.MinioProperties.bucketName)
                            .object(fileName)
                            .build()
            ));
        } catch (Exception e) {
            throw new FileNotFoundException(String.format("Failed to retrieve file: %s", fileName));
        }
    }

    public ResponseEntity<Resource> getProfilePhotoResponse(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return ResponseEntity.notFound().build();
        }

        Resource photoResource = getPhoto(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName))
                .body(photoResource);
    }

    public boolean fileExists(String fileName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(MinioConfig.MinioProperties.bucketName)
                            .object(fileName)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new RuntimeException(String.format("Error checking if file exists: %s", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error checking if file exists: %s", e.getMessage()));
        }
    }

    private void ensureBucketExists() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(MinioConfig.MinioProperties.bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(MinioConfig.MinioProperties.bucketName).build());
            }
        } catch (Exception e) {
            throw new FileUploadException(String.format("Error checking or creating bucket: %s", e.getMessage()));
        }
    }
}