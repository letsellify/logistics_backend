package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/8/25
 */


public record LogisticVendorResource(
        String email,
        String fullName,
        String phoneNumber,
        String whatsAppPhoneNumber
) {
}
