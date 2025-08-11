package com.letsellify.logistics.components.logistics.core.kycManagement.rest.dto;

import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/11/25
 * Time:11:39
 */

@Data
public class KycDocumentTypeDto {
    @NotNull
    KycDocumentType kycDocument;
}
