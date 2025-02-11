package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity.LogisticsItemImageEntity;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:18:01
 */

@Data
public class LogisticsItemImage {
    private final LogisticsItemImageEntity entity;

    private String imageId;
    private String imagefilePath;
    private String vendorUsername;

    public LogisticsItemImage(final LogisticsItemImageEntity entity) {
        this.entity = entity;
        this.imageId = entity.getId();
        this.imagefilePath = entity.getImageFilePath();
        this.vendorUsername = entity.getVendorUsername();
    }
}
