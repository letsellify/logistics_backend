package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:31
 */


@Data
@AllArgsConstructor
public class LogisticsItem {
    private final String itemName;
    private final String itemDescription;
    private final List<LogisticsItemImage> images;
}
