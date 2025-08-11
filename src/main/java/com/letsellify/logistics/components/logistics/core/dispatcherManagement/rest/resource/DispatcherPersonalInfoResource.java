package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:36
 */


public record DispatcherPersonalInfoResource(
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address
) {
}
