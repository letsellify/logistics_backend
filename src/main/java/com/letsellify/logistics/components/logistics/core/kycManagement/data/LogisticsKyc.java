package com.letsellify.logistics.components.logistics.core.kycManagement.data;

import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.KycEntity;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:2/3/25
 * Time:08:45
 */

@Data
public class LogisticsKyc {
    private final KycEntity kycEntity;

    private String kycName;

    private String kycIdentification;

    private String kycFilePath;

    public LogisticsKyc(final KycEntity kycEntity) {
        this.kycEntity = kycEntity;
        this.kycName = kycEntity.getKycDocument().getValue();
        this.kycIdentification = kycEntity.getId();
        this.kycFilePath = kycEntity.getFilePath();
    }

//    public KycIdentifier getResource() {
//        return KycDataMapper.INSTANCE.toResource();
//    }
}
