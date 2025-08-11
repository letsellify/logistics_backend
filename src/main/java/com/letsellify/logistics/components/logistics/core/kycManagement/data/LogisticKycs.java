package com.letsellify.logistics.components.logistics.core.kycManagement.data;

import com.letsellify.logistics.components.logistics.core.kycManagement.database.entity.UserKycCollectionEntity;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:21:47
 */

@Getter
public class LogisticKycs {
    private final UUID kycId;
    private final List<LogisticKycDocument> kycs;

    public LogisticKycs(final UserKycCollectionEntity userKycCollectionEntity) {
        this.kycId = userKycCollectionEntity.getId();
        this.kycs = userKycCollectionEntity
                .getKycs()
                .stream()
                .map(LogisticKycDocument::new).toList();
    }
}
