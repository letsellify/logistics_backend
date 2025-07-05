package com.letsellify.logistics.components.logistic.core.request.data;

import com.letsellify.logistics.components.logistic.core.request.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticItemImageEntity;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemImageResource;

import lombok.Getter;

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
    private final String senderUsername;

    public LogisticsItemImage(final LogisticItemImageEntity entity) {
        this.entity = entity;
        this.imageId = entity.getId();
        this.imageFilePath = entity.getImageFilePath();
        this.senderUsername = entity.getSenderUsername();
    }

    public LogisticItemImageResource getResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }

}
