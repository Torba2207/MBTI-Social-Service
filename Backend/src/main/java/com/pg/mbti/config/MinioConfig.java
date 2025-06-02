package com.pg.mbti.config;

import com.pg.mbti.config.property.MinioProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for MinIO object storage service.
 * MinIO provides S3-compatible storage for application files.
 */
@Configuration
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperties minioProperties;

    // ===== Storage Client =====

    @Bean
    public MinioClient minioClient() {
        // Creates MinIO client for file operations (upload/download)
        // using configured endpoint and credentials
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}