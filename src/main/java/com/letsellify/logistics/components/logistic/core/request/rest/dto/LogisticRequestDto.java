package com.letsellify.logistics.components.logistic.core.request.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/23/25
 * Time:08:47
 */

@Data
public class LogisticRequestDto {
    @NotBlank
    String shippingRequestId;
}
