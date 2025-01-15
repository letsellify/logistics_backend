package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:10:14
 */

@Data
public class PaystackPaymentInitializationResource {
    @NotBlank
    private final String amount;
}
