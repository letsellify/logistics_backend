package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherPersonalInformationResource(
        String name,
        String state,
        String lga,
        String address
) {
}
