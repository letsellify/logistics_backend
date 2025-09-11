package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;


import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


public record DispatcherLgaPreferenceResource(
        UUID preferenceId,
        String pickUpLga,
        String dropOffLga
) {
}
