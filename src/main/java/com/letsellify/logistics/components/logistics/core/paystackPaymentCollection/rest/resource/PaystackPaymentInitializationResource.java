package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
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
