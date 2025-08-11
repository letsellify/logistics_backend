package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;


/**
 * Author: Ahmad Buba
 * Date:8/4/25
 */


public record BusinessInformationDto(
        String businessName,
        String businessOfficeAddress,
        String state,
        String lg
) {
}
