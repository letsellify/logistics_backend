package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:15:54
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackListBanksResponse {
    private boolean status;
    private String message;
    private List<Bank> data;
    private Meta meta;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bank {
        private String name;
        private String slug;
        private String code;
        private String longcode;
        private String gateway;

        @JsonProperty("pay_with_bank")
        private boolean payWithBank;

        private boolean active;

        @JsonProperty("is_deleted")
        private boolean isDeleted;

        private String country;
        private String currency;
        private String type;
        private int id;

        @JsonProperty("createdAt")
        private Instant createdAt;

        @JsonProperty("updatedAt")
        private Instant updatedAt;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private String next;
        private String previous;

        @JsonProperty("perPage")
        private int perPage;
    }
}
