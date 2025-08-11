package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:21:36
 */

public record DispatcherInfoDto(
        DispatcherPersonalInfoDto personalInfo,
        DispatcherContactInfoDto contactInfo,
        DispatchDetailDto dispatchDetail
) {
}
