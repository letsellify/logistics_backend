package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:20:41
 */

@Data
@AllArgsConstructor
public class PaystackInitiateTransactionResponse {
    private boolean status;
    private String message;
    private TransactionData data;


    // Nested static class for TransactionData
    @Data
    @AllArgsConstructor
    public static class TransactionData {

        @JsonProperty("authorization_url")
        private String authorizationUrl;

        @JsonProperty("access_code")
        private String accessCode;

        private String reference;

    }
}
