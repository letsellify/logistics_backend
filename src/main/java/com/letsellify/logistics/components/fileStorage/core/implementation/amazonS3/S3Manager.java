package com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3;

import java.io.IOException;
import java.time.Duration;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.config.S3ConfigProperties;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:31
 */

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class S3Manager implements FileStorageManager {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3ConfigProperties configProperties;

    @Override
    public String storeFile(final @NonNull StorageType storageType, final @NonNull String username, final @NonNull String fileType, final @NonNull MultipartFile file) throws LogisticsS3IOException {
        final String key = storageType + "/" + username + "/" + fileType + "/" + file.getOriginalFilename();
        log.info("s3 file path structure: {}", key);
        try {
            this.s3Client.putObject(
              PutObjectRequest.builder()
                              .bucket(this.configProperties.bucketName())
                              .key(key)
                              .build(),
              RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (final IOException e) {
            throw new LogisticsS3IOException("Failed to upload file to S3", e);
        }

        return key; // Return the key to track the uploaded file.
    }

    @Override
    public String generatePresignedUrl(final @NonNull String fileReference) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                                                  .bucket(this.configProperties.bucketName())
                                                                  .key(fileReference)
                                                                  .build();

        final PresignedGetObjectRequest presignedRequest = this.s3Presigner.presignGetObject(builder -> builder
                                                                                               .getObjectRequest(getObjectRequest)
                                                                                               .signatureDuration(Duration.ofMinutes(this.configProperties.presignedUrlExpiration())));

        return presignedRequest.url().toString();
    }

    @Override
    public void deleteFile(final @NonNull String fileReference) {
        this.s3Client.deleteObject(
          DeleteObjectRequest.builder()
                             .bucket(this.configProperties.bucketName())
                             .key(fileReference)
                             .build()
        );
    }

}
