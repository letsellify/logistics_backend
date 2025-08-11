package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:12:02
 */

public record DispatcherResource(
        String name,
        String email,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address
) {
}
