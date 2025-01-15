package com.letsellify.logistics.components.logistics.core.kyc.data;

import com.letsellify.logistics.components.logistics.core.kyc.dataMapper.KycDataMapper;
import com.letsellify.logistics.components.logistics.core.kyc.database.entity.KycEntity;
import com.letsellify.logistics.components.logistics.core.kyc.rest.resource.KycDocumentUploadResource;
import com.letsellify.logistics.components.logistics.core.kyc.rest.resource.KycResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:22
 */

@Data
public class LogisticsKyc {
    private final KycEntity kycEntity;

    private KycDocument kycDocument;
    private String filePath;

    public LogisticsKyc(final KycEntity kycEntity) {
       this.kycEntity = kycEntity;
       this.kycDocument = kycEntity.getKycDocument();
       this.filePath = kycEntity.getFilePath();
    }

    public KycResource toKycResource() {
        return KycDataMapper.INSTANCE.resource(this);
    }

    public KycDocumentUploadResource toKycDocumentUploadResource() {
        return KycDataMapper.INSTANCE.toKycDocumentUploadResource(this);
    }
}
