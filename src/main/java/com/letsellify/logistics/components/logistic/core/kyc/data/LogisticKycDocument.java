package com.letsellify.logistics.components.logistic.core.kyc.data;

import com.letsellify.logistics.components.logistic.core.kyc.database.entity.KycEntity;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:2/3/25
 * Time:08:45
 */

@Data
public class LogisticKycDocument {
    private KycDocumentType kycDocument;

    private String kycIdentification;

    private String kycFilePath;

    public LogisticKycDocument(final KycEntity kycEntity) {
        this.kycDocument = kycEntity.getKycDocument();
        this.kycIdentification = kycEntity.getId();
        this.kycFilePath = kycEntity.getFilePath();
    }

//    public KycIdentifier getResource() {
//        return KycDataMapper.INSTANCE.toResource();
//    }
}
