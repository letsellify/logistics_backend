package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.LogisticItemImageEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemImageResource;
import lombok.Getter;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:18:01
 */


@Getter
public final class LogisticsItemImage {
    private final LogisticItemImageEntity entity;
    private final String imageId;
    private final String imageFilePath;
    private final UUID senderId;

    public LogisticsItemImage(final LogisticItemImageEntity entity) {
        this.entity = entity;
        this.imageId = entity.getId();
        this.imageFilePath = entity.getImageFilePath();
        this.senderId = entity.getSenderId();
    }

    public LogisticItemImageResource getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }

}
