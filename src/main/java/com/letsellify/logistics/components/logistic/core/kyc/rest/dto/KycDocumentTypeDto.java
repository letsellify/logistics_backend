package com.letsellify.logistics.components.logistic.core.kyc.rest.dto;

import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

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
