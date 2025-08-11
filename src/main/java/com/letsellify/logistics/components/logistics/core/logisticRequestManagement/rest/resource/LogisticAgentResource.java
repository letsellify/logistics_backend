package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/8/25
 */


public record LogisticAgentResource(
        String email,
        String name,
        String state,
        String lga,
        String address,
        String whatsAppPhone,
        String phone,
        String storeState,
        String storeLga,
        String storeAddress
) {
}
