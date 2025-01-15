package com.letsellify.logistics.components.logistics.core.kyc.rest.resource;

import com.letsellify.logistics.components.logistics.core.kyc.data.KycDocument;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/11/25
 * Time:11:41
 */

@Data
public class KycResource {
    @NotNull
    KycDocument kycDocument;
}
