package com.pg.mbti.service;
import com.pg.mbti.config.property.MinioProperties;
import com.pg.mbti.exception.FileNotFoundException;
import com.pg.mbti.exception.FileUploadException;
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
import lombok.extern.slf4j.Slf4j; // Import SLF4J for logging

import java.util.UUID;

/**
 * Service class for managing photo uploads and retrievals using MinIO.
 */
@Service
@RequiredArgsConstructor
@Slf4j // Enable logging for this class
public class PhotoService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Value("${image.default.path}")
    private String defaultImagePath;

    /**
     * Uploads a multipart file to the MinIO storage.
     * Generates a unique file name unless it's the default image.
     *
     * @param file The {@link MultipartFile} to upload.
     * @return The unique file name generated for the uploaded file.
     * @throws FileUploadException If the file upload fails or the file name is invalid.
     */
    public String uploadPhoto(MultipartFile file) {
        log.info("Attempting to upload photo: {}", file.getOriginalFilename()); // Log upload attempt
        try {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                log.warn("File upload failed: Invalid file name for an uploaded photo."); // Log invalid file name
                throw new FileUploadException("Invalid file name");
            }

            String fileName = originalFilename.equals(defaultImagePath)
                    ? originalFilename
                    : UUID.randomUUID() + "_" + originalFilename;
            log.debug("Generated file name for upload: {}", fileName); // Log generated file name

            ensureBucketExists();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("Photo uploaded successfully with file name: {}", fileName); // Log successful upload
            return fileName;
        } catch (Exception e) {
            log.error("Failed to upload file {}: {}", file.getOriginalFilename(), e.getMessage()); // Log upload failure
            throw new FileUploadException(String.format("Failed to upload file: %s", file.getOriginalFilename()));
        }
    }

    /**
     * Retrieves a photo as a {@link Resource} from MinIO using its file name.
     *
     * @param fileName The name of the file to retrieve.
     * @return A {@link Resource} representing the photo.
     * @throws FileNotFoundException If the file cannot be retrieved from MinIO.
     */
    public Resource getPhoto(String fileName) {
        log.info("Attempting to retrieve photo: {}", fileName); // Log photo retrieval attempt
        try {
            return new InputStreamResource(minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .build()
            ));
        } catch (Exception e) {
            log.error("Failed to retrieve file {}: {}", fileName, e.getMessage()); // Log retrieval failure
            throw new FileNotFoundException(String.format("Failed to retrieve file: %s", fileName));
        }
    }

    /**
     * Retrieves a profile photo as a {@link ResponseEntity<Resource>} for HTTP response.
     * Handles cases where the file name is empty.
     *
     * @param fileName The name of the profile photo file.
     * @return A {@link ResponseEntity} containing the photo resource, or not found if the file name is empty.
     */
    public ResponseEntity<Resource> getProfilePhotoResponse(String fileName) {
        log.info("Preparing profile photo response for file: {}", fileName); // Log response preparation
        if (StringUtils.isEmpty(fileName)) {
            log.warn("Cannot retrieve profile photo: file name is empty."); // Log empty file name
            return ResponseEntity.notFound().build();
        }

        Resource photoResource = getPhoto(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName))
                .body(photoResource);
    }

    /**
     * Checks if a file exists in the MinIO bucket.
     *
     * @param fileName The name of the file to check.
     * @return {@code true} if the file exists, {@code false} otherwise.
     * @throws RuntimeException If an unexpected error occurs during the check.
     */
    public boolean fileExists(String fileName) {
        log.debug("Checking if file exists: {}", fileName); // Log file existence check
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .build()
            );
            log.debug("File {} exists.", fileName); // Log file exists
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                log.debug("File {} does not exist (NoSuchKey error).", fileName); // Log file not found by NoSuchKey
                return false;
            }
            log.error("Error checking if file {} exists: {}", fileName, e.getMessage()); // Log other MinIO errors
            throw new RuntimeException(String.format("Error checking if file exists: %s", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error checking if file {} exists: {}", fileName, e.getMessage()); // Log unexpected errors
            throw new RuntimeException(String.format("Error checking if file exists: %s", e.getMessage()));
        }
    }

    /**
     * Ensures that the MinIO bucket specified in properties exists.
     * If the bucket does not exist, it attempts to create it.
     *
     * @throws FileUploadException If there's an error checking or creating the bucket.
     */
    private void ensureBucketExists() {
        log.debug("Ensuring MinIO bucket '{}' exists.", minioProperties.getBucket()); // Log bucket existence check
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build())) {
                log.info("MinIO bucket '{}' does not exist. Creating it now.", minioProperties.getBucket()); // Log bucket creation
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
                log.info("MinIO bucket '{}' created successfully.", minioProperties.getBucket()); // Log successful bucket creation
            } else {
                log.debug("MinIO bucket '{}' already exists.", minioProperties.getBucket()); // Log bucket already exists
            }
        } catch (Exception e) {
            log.error("Error checking or creating MinIO bucket '{}': {}", minioProperties.getBucket(), e.getMessage()); // Log bucket error
            throw new FileUploadException(String.format("Error checking or creating bucket: %s", e.getMessage()));
        }
    }
}