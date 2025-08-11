package com.letsellify.logistics.components.logistics.core.kycManagement;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.LogisticKycDocument;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.LogisticKycs;
import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.KycEntity;
import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.UserKycCollectionEntity;
import com.letsellify.logistics.components.logistics.core.kycManagement.database.repository.KycRepository;
import com.letsellify.logistics.components.logistics.core.kycManagement.database.repository.UserKycCollectionRepository;
import com.letsellify.logistics.components.logistics.core.kycManagement.exception.NoKycRecordFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:19
 */


// holds all kyc rules for various user types you upload it determines, wrong role or already approved kyc results in exception
@Component
@RequiredArgsConstructor
@Slf4j
public class KycManager {
    private final UserKycCollectionRepository userKycCollectionRepository;
    private final KycRepository kycRepository;
    private final FileStorageManager fileStorageManager;

//    @Transactional
//    public LogisticsKyc setKycDocumentType(final @NonNull UUID userId, final @NonNull KycDocument kycDocument) throws UserNotFoundException {
//        // Retrieve or initialize the KYC entity
//        final KycEntity entity = this.kycRepository.findByUserId(userId)
//                                                   .orElseGet(() -> this.initializeNewKycEntity(userId, kycDocument));
//
//        // Handle existing KYC entity
//        if (entity.getFilePath() != null) {
//            throw new RuntimeException("File already exists for user: " + userId +
//                                                 ". Delete the current file before setting a new KYC document type.");
//        }
//
//        // Update the existing entity
//        entity.setKycDocument(kycDocument);
//        this.kycRepository.save(entity);
//
//        return new LogisticsKyc(entity);
//    }


    // get user kyc(username)
    // check for kyc using username: if exist, it should contain userName, userId, and s3Path;
    // ask s3 manager to getFile or preSignedUrl using s3 path: should return the file or url
    // wrap into Kyc(email, kycDocument, file) and return


//
//    @Transactional
//    public LogisticsKyc uploadKycDocument(final @NonNull UUID userId, final @NonNull MultipartFile multipartFile) throws UserNotFoundException, LogisticsS3IOException, KycResourceNotFoundException, KycBadRequestException {
//        // Retrieve the user's KYC entity
//        final KycEntity entity = this.kycRepository.findByUserId(userId)
//                                                   .orElseThrow(() -> new KycResourceNotFoundException("KYC record not found"));
//
//        // Ensure the KYC document type is set
//        if (entity.getKycDocument() == null) {
//            throw new KycBadRequestException("KYC document type is not set");
//        }
//
//        if (entity.getFilePath() != null && !entity.getFilePath().isEmpty()) {
//            this.fileStorageManager.deleteFile(entity.getFilePath());
//        }
//        final String filePath = this.fileStorageManager.storeFile(StorageType.KYC,entity.getKycDocument().getValue(),multipartFile);
//        entity.setFilePath(filePath);
//        this.kycRepository.save(entity);
//        return new LogisticsKyc(entity);
//    }

    // enforce kyc rules based on user type here
    @Transactional
    public LogisticKycDocument uploadKyc(final @NonNull String userEmail, final @NonNull LogisticAppRole userType, final @NonNull KycDocumentType kycDocument, final @NonNull MultipartFile multipartFile) throws IOException {
        final UserKycCollectionEntity kycCollectionEntity = this.userKycCollectionRepository
                .findByUserEmail(userEmail).orElseGet(() -> UserKycCollectionEntity.getInstance(userEmail, userType));
        final String filePath = this.fileStorageManager.storeFile(StorageType.KYC, userEmail, multipartFile);
        log.info("the filePath on s3, {}", filePath);
        final KycEntity kycEntity = new KycEntity(kycDocument, filePath);
        kycCollectionEntity.addKyc(kycEntity);
        this.userKycCollectionRepository.save(kycCollectionEntity);
        return new LogisticKycDocument(kycEntity);
    }


    @Transactional
    public void deleteKyc(final @NonNull String userEmail, final @NonNull String kycIdentifier) throws NoKycRecordFoundException {
        final KycEntity kycEntity = this.kycRepository.findById(kycIdentifier)
                .orElseThrow(() -> new NoKycRecordFoundException("No such kyc record exists"));

        final UserKycCollectionEntity userKycCollection = kycEntity.getUserKycCollection();

        if (!userKycCollection.getUserEmail().equals(userEmail)) {
            throw new IllegalArgumentException("KYC does not belong to the provided user");
        }

        // Remove from parent collection (triggers orphan removal)
        userKycCollection.removeKyc(kycEntity);
        this.userKycCollectionRepository.save(userKycCollection);  // Persist changes

        // Delete the file from S3
        this.fileStorageManager.deleteFile(kycEntity.getFilePath());
    }

    public String viewKyc(final @NonNull String kycIdentifier) throws NoKycRecordFoundException {
        final KycEntity kycEntity = this.kycRepository.findById(kycIdentifier)
                .orElseThrow(() -> new NoKycRecordFoundException("No such kyc record exists"));
        return this.fileStorageManager.generatePresignedUrl(kycEntity.getFilePath());
    }


    private UserKycCollectionEntity initializeNewKycEntity(final @NonNull String userEmail, final @NonNull LogisticAppRole userType, final @NonNull KycDocumentType kycDocument) {
        final UserKycCollectionEntity newEntity = UserKycCollectionEntity.getInstance(userEmail, userType);
        this.userKycCollectionRepository.save(newEntity);
        return newEntity;
    }

    public LogisticKycs findDispatcherKyc(final @NonNull String email) throws NoKycRecordFoundException {
        final UserKycCollectionEntity kycCollectionEntity = this.userKycCollectionRepository.findByUserEmailAndUserType(email, LogisticAppRole.DISPATCHER)
                .orElseThrow(() -> new NoKycRecordFoundException("No kyc record found for this user " + email));
        return new LogisticKycs(kycCollectionEntity);
    }

    @Transactional(readOnly = true)
    public LogisticKycs findKyc(final @NonNull UUID id) throws NoKycRecordFoundException {
        final UserKycCollectionEntity kycCollectionEntity = this.userKycCollectionRepository.findById(id)
                .orElseThrow(() -> new NoKycRecordFoundException("No kyc record found with this Id"));
        return new LogisticKycs(kycCollectionEntity);
    }

    public void approveKyc(final @NonNull UUID id) throws NoKycRecordFoundException {
        final UserKycCollectionEntity kycCollectionEntity = this.userKycCollectionRepository.findById(id)
                .orElseThrow(() -> new NoKycRecordFoundException("No kyc record found with this Id"));
        if (!kycCollectionEntity.isApproved()) {
            kycCollectionEntity.setApproved(true);
        }
        this.userKycCollectionRepository.save(kycCollectionEntity);
    }

    public LogisticKycs findAgentKyc(final @NonNull String email) throws NoKycRecordFoundException {
        final UserKycCollectionEntity kycCollectionEntity = this.userKycCollectionRepository.findByUserEmailAndUserType(email, LogisticAppRole.AGENT)
                .orElseThrow(() -> new NoKycRecordFoundException("No kyc record found for this user " + email));
        return new LogisticKycs(kycCollectionEntity);
    }

}
