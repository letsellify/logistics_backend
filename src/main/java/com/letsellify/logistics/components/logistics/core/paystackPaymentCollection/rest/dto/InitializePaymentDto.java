package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:10:18
 */

@Data
public class InitializePaymentDto {
    // add custom validator to validate as digit and non negative amount greater than 1000
    @NotBlank
    private final String amount;
}
