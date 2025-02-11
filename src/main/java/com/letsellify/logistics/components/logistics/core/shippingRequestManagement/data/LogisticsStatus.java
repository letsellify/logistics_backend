package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data;

/**
 * @author AHMAD BUBA
 * Date:2/11/25
 * Time:15:03
 */

public enum LogisticsStatus {
    REQUESTED,
    DISPATCH_ACCEPTED,
    STORAGE_ACCEPTED,
    ACCEPTED_FOR_DISPATCH_AND_STORAGE,
    COLLECTED_BY_DISPATCHER,
    COLLECTED_FOR_STORAGE,
    LOGISTICS_COLLECTED,
    COMPLETED
}
