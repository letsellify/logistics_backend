package com.letsellify.logistics.components.fileStorage.core;

import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:54
 */

public interface FileStorageManager {
    String storeFile(StorageType storageType, String fileType, MultipartFile file) throws LogisticsS3IOException; // Returns a key or reference to the stored file

    String generatePresignedUrl(String fileReference);

    void deleteFile(String fileReference);
}
