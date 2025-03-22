package com.letsellify.logistics.components.logistic.core.kyc.data;

import com.letsellify.logistics.components.logistic.core.kyc.dataMapper.KycDataMapper;
import com.letsellify.logistics.components.logistic.core.kyc.database.entity.UserKycCollectionEntity;
import com.letsellify.logistics.components.logistic.core.kyc.rest.resource.KycDocumentUploadResource;
import com.letsellify.logistics.components.logistic.core.kyc.rest.resource.KycResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:22
 */

@Data
public class UserLogisticsKycCollection {
    private final UserKycCollectionEntity kycCollectionEntity;

//    private String userEmail;
//    private KycDocument kycDocu;
//    private String filePath;
//
//    public UserLogisticsKycCollection(final UserKycCollectionEntity kycCollectionEntity) {
//       this.kycCollectionEntity = kycCollectionEntity;
//       this.userEmail = kycCollectionEntity.getUserEmail();
//       this.kycDocument = kycCollectionEntity.getKycDocument();
//       this.filePath = kycCollectionEntity.getFilePath();
//    }

    public KycResource toKycResource() {
        return KycDataMapper.INSTANCE.resource(this);
    }

    public KycDocumentUploadResource toKycDocumentUploadResource() {
        return KycDataMapper.INSTANCE.toKycDocumentUploadResource(this);
    }
}
