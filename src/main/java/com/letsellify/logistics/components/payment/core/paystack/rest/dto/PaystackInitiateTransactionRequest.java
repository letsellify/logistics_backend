package com.letsellify.logistics.components.payment.core.paystack.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:20:46
 */

@Data
@AllArgsConstructor
public class PaystackInitiateTransactionDto {
    private final String email;
    private final String amount;
}
