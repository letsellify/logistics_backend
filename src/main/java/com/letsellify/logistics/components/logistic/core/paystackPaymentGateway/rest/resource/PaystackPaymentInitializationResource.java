package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
