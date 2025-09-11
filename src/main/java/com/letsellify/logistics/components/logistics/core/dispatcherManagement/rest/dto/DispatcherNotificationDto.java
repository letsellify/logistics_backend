package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto;


import jakarta.validation.constraints.NotNull;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


public record DispatcherNotificationDto(
        @NotNull
        boolean all
) {
}
