package com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:21:01
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackResolveBankAccountResponse {
    private boolean status;
    private String message;
    private AccountDetail data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountDetail {
        @JsonProperty("account_number")
        private String accountNumber;
        @JsonProperty("account_name")
        private String accountName;
        @JsonProperty("bank_id")
        private int bankId;
    }
}
