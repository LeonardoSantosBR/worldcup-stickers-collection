package com.leonardo.worldcup_stickers.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
            S3Client s3Client,
            @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String upload(MultipartFile file, String keyPrefix) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The file cannot be empty.");
        }

        String key = buildKey(keyPrefix, file.getOriginalFilename());
        PutObjectRequest.Builder request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key);

        if (file.getContentType() != null && !file.getContentType().isBlank()) {
            request.contentType(file.getContentType());
        }

        try {
            s3Client.putObject(request.build(), RequestBody.fromBytes(file.getBytes()));
        } catch (IOException exception) {
            throw new IllegalStateException("The uploaded file could not be read.", exception);
        }

        return key;
    }

    public String createPresignedGetUrl(String key, Duration expiration) {
        try (S3Presigner presigner = S3Presigner.create()) {
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(expiration)
                            .getObjectRequest(GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(key)
                                    .build())
                            .build());
            return presignedRequest.url().toString();
        }
    }

    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    private String buildKey(String keyPrefix, String originalFilename) {
        String prefix = keyPrefix == null || keyPrefix.isBlank()
                ? ""
                : keyPrefix.replaceAll("^/+|/+$", "") + "/";
        String extension = "";
        if (originalFilename != null) {
            int extensionIndex = originalFilename.lastIndexOf('.');
            if (extensionIndex >= 0 && extensionIndex < originalFilename.length() - 1) {
                extension = originalFilename.substring(extensionIndex).replaceAll("[^a-zA-Z0-9.]", "");
            }
        }
        return prefix + UUID.randomUUID() + extension;
    }
}
