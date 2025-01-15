package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:20:46
 */

@Data
@AllArgsConstructor
public class PaystackInitiateTransactionRequest {
    private final String email;
    private final String amount;
}
