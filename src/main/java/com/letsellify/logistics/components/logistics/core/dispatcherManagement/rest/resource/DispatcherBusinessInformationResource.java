package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;


/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherBusinessInformationResource(
        String name,
        String address,
        String state,
        String lga
) {
}
