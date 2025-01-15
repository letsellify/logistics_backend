package com.letsellify.logistics.components.logistics.core.kyc.rest.dto;

import com.letsellify.logistics.components.logistics.core.kyc.data.KycDocument;

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
    KycDocument kycDocument;
}
