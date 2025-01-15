package com.letsellify.logistics.components.logistics.core.kyc;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;
import com.letsellify.logistics.components.logistics.core.kyc.data.KycDocument;
import com.letsellify.logistics.components.logistics.core.kyc.data.LogisticsKyc;
import com.letsellify.logistics.components.logistics.core.kyc.database.entity.KycEntity;
import com.letsellify.logistics.components.logistics.core.kyc.database.repository.KycRepository;
import com.letsellify.logistics.components.logistics.core.kyc.exception.KycBadRequestException;
import com.letsellify.logistics.components.logistics.core.kyc.exception.KycResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.user.exception.UserNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:19
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class KycManager {
    private final KycRepository kycRepository;
    private final FileStorageManager fileStorageManager;

    @Transactional
    public LogisticsKyc setKycDocumentType(final @NonNull UUID userId, final @NonNull KycDocument kycDocument) throws UserNotFoundException {
        // Retrieve or initialize the KYC entity
        final KycEntity entity = this.kycRepository.findByUserId(userId)
                                                   .orElseGet(() -> this.initializeNewKycEntity(userId, kycDocument));

        // Handle existing KYC entity
        if (entity.getFilePath() != null) {
            throw new RuntimeException("File already exists for user: " + userId +
                                                 ". Delete the current file before setting a new KYC document type.");
        }

        // Update the existing entity
        entity.setKycDocument(kycDocument);
        this.kycRepository.save(entity);

        return new LogisticsKyc(entity);
    }



    // get user kyc(username)
    // check for kyc using username: if exist, it should contain userName, userId, and s3Path;
    // ask s3 manager to getFile or preSignedUrl using s3 path: should return the file or url
    // wrap into Kyc(email, kycDocument, file) and return



    @Transactional
    public LogisticsKyc uploadKycDocument(final @NonNull UUID userId, final @NonNull MultipartFile multipartFile) throws UserNotFoundException, LogisticsS3IOException, KycResourceNotFoundException, KycBadRequestException {
        // Retrieve the user's KYC entity
        final KycEntity entity = this.kycRepository.findByUserId(userId)
                                                   .orElseThrow(() -> new KycResourceNotFoundException("KYC record not found"));

        // Ensure the KYC document type is set
        if (entity.getKycDocument() == null) {
            throw new KycBadRequestException("KYC document type is not set");
        }

        if (entity.getFilePath() != null && !entity.getFilePath().isEmpty()) {
            this.fileStorageManager.deleteFile(entity.getFilePath());
        }
        final String filePath = this.fileStorageManager.storeFile(StorageType.KYC,entity.getKycDocument().getValue(),multipartFile);
        entity.setFilePath(filePath);
        this.kycRepository.save(entity);
        return new LogisticsKyc(entity);
    }


    private KycEntity initializeNewKycEntity(final @NonNull UUID userId, final @NonNull KycDocument kycDocument) {
        final KycEntity newEntity = KycEntity.getInstance(userId, kycDocument);
        this.kycRepository.save(newEntity);
        return newEntity;
    }
}
