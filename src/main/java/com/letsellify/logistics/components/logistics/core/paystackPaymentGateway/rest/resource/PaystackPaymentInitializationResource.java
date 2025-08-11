package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:10:14
 */

@Data
public class PaystackPaymentInitializationResource {
    @NotNull
    private final BigDecimal amount;
}
