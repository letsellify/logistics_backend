package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */


public record AgentProfilePictureDto(
        @Schema(
                type = "string",
                format = "binary",
                description = "Image file to upload as profile picture"
        )
        @NonNull
        MultipartFile file
) {
}
