package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:10:18
 */

@Data
public class InitializePaymentDto {
    // add custom validator to validate as digit and non negative amount greater than 1000
    @NotNull
    private final BigDecimal amount;
}
