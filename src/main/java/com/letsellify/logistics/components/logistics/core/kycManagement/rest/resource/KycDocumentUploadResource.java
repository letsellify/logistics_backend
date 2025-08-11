package com.letsellify.logistics.components.logistics.core.kycManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/11/25
 * Time:12:15
 */

@Data
public class KycDocumentUploadResource {
    @NotNull
    @JsonProperty("download_url")
    private String filePath;
}
