package com.letsellify.logistics.common.data;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:2/3/25
 * Time:15:43
 */

@Data
public class LogisticsShippingLocation {
    private final String currentState;
    private final String currentLga;
    private final String shippingState;
    private final String shippingLga;
}
