package com.letsellify.logistics.components.fileStorage.core;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:54
 */

public interface FileStorageManager {
    String storeFile(StorageType storageType, String username, MultipartFile file) throws IOException; // Returns a key or reference to the stored file

    String generatePresignedUrl(String fileReference);

    void deleteFile(String fileReference);

    default void validateImageFile(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new LogisticsBadRequestException("Uploaded file is empty.");
        }

        final String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new LogisticsBadRequestException("Only image files are allowed.");
        }

        // Optional: Restrict file size (e.g., max 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new LogisticsBadRequestException("File size exceeds the maximum allowed limit of 5MB.");
        }
    }
}
