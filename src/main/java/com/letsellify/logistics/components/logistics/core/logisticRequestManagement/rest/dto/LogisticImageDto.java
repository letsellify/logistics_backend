package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Ahmad Buba
 * Date: 8/14/25
 */

public record LogisticImageDto(
        @Schema(type = "string", format = "binary", description = "Image file to upload")
        @NonNull
        MultipartFile file
) {
}
