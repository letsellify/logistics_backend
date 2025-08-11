package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/8/25
 */


public record LogisticDispatcherResource(
        String name,
        String email,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address
) {
}
