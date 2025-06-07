package com.pg.mbti.util;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for MinIO operations.
 * Provides helper methods for common MinIO tasks such as bucket initialization.
 */
@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class MinioUtils {

    /**
     * Initializes a MinIO bucket, creating it if it doesn't exist.
     *
     * @param minioClient the MinIO client used to perform operations
     * @param bucketName the name of the bucket to initialize
     * @throws RuntimeException if bucket initialization fails due to MinIO errors or other exceptions
     */
    public static void initializeBucket(MinioClient minioClient, String bucketName) {
        log.debug("Initializing MinIO bucket: {}", bucketName);

        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());

            if (!bucketExists) {
                log.info("MinIO bucket '{}' does not exist. Creating bucket...", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO bucket '{}' created successfully", bucketName);
            } else {
                log.debug("MinIO bucket '{}' already exists", bucketName);
            }
        } catch (MinioException e) {
            log.error("MinIO error during bucket initialization for '{}': {}", bucketName, e.getMessage());
            throw new RuntimeException("Failed to initialize MinIO bucket: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during bucket initialization for '{}': {}", bucketName, e.getMessage());
            throw new RuntimeException("Unexpected error during MinIO bucket initialization: " + e.getMessage(), e);
        }
    }
}