package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:11:32
 */

@Data
public class ChargeSuccessPayload {
    private String event;
    private DataPayload data;

    @Data
    public static class DataPayload {
        private long id;
        private String domain;
        private String status;
        private String reference;
        private double amount;
        private String message;
        @JsonProperty("gateway_response")
        private String gatewayResponse;
        @JsonProperty("paid_at")
        private LocalDateTime paidAt;
        @JsonProperty("created_at")
        private LocalDateTime createdAt;
        private String channel;
        private String currency;
        @JsonProperty("ip_address")
        private String ipAddress;
        private JsonNode metadata;
        private Log log;
        private String fees;
        private Customer customer;
        private Authorization authorization;
        private Plan plan;

        @Data
        public static class Plan {
            private Long id;
            private String name;
            @JsonProperty("plan_code")
            private String planCode;
            private String description;
            private Integer amount;
            private String interval;
            @JsonProperty("send_invoices")
            private boolean sendInvoices;
            @JsonProperty("send_sms")
            private boolean sendSms;
            private String currency;
        }

        @Data
        public static class Log {
            @JsonProperty("time_spent")
            private int timeSpent;
            private int attempts;
            private String authentication;
            private int errors;
            private boolean success;
            private boolean mobile;
            private List<String> input;
            private String channel;
            private List<History> history;

            @Data
            public static class History {
                private String type;
                private String message;
                private int time;
            }
        }

        @Data
        public static class Customer {
            private int id;
            @JsonProperty("first_name")
            private String firstName;
            @JsonProperty("last_name")
            private String lastName;
            private String email;
            @JsonProperty("customer_code")
            private String customerCode;
            private String phone;
            private JsonNode metadata;
            @JsonProperty("risk_action")
            private String riskAction;
        }

        @Data
        public static class Authorization {
            @JsonProperty("authorization_code")
            private String authorizationCode;
            private String bin;
            private String last4;
            @JsonProperty("exp_month")
            private String expMonth;
            @JsonProperty("exp_year")
            private String expYear;
            @JsonProperty("card_type")
            private String cardType;
            private String bank;
            @JsonProperty("country_code")
            private String countryCode;
            private String brand;
            @JsonProperty("account_name")
            private String accountName;
        }
    }
}
