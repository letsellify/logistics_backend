package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto.ChargeSuccessPayload;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:14:21
 */

@Entity
@Data
public class PaystackChargeSuccessDataEntity {
    @Id
    private long id;
    private String domain;
    private String status;
    private String reference;
    private double amount;
    private String message;
    private String gatewayResponse;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    @Column(insertable = false, updatable = false)
    private String channel;
    private String currency;
    private String ipAddress;
    @Column(columnDefinition = "jsonb", insertable = false, updatable = false)
    private String metadata;

    @Embedded
    private Log log;

    private String fees;

    @Embedded
    private Customer customer;

    @Embedded
    private Authorization authorization;

    @Embedded
    private Plan plan; // Changed from Object to String

    // No-arg constructor for JPA
    public PaystackChargeSuccessDataEntity() {
    }

    // Constructor for DataPayload that takes the payload
    public PaystackChargeSuccessDataEntity(final ChargeSuccessPayload.DataPayload payload) {
        this.id = payload.getId();
        this.domain = payload.getDomain();
        this.status = payload.getStatus();
        this.reference = payload.getReference();
        this.amount = payload.getAmount();
        this.message = payload.getMessage();
        this.gatewayResponse = payload.getGatewayResponse();
        this.paidAt = payload.getPaidAt();
        this.createdAt = payload.getCreatedAt();
        this.channel = payload.getChannel();
        this.currency = payload.getCurrency();
        this.ipAddress = payload.getIpAddress();
        this.metadata = payload.getMetadata().toString();
        this.fees = payload.getFees();
        this.log = new Log(payload.getLog()); // Deep copy of Log
        this.customer = new Customer(payload.getCustomer()); // Deep copy of Customer
        this.authorization = new Authorization(payload.getAuthorization()); // Deep copy of Authorization
        this.plan = new Plan(payload.getPlan());
    }


    @Embeddable
    @Data
    public static class Plan {
        @Column(insertable = false, updatable = false)
        private Long id;
        private String name;
        private String planCode;
        private String description;
        @Column(name = "plan_amount") // Change the column name
        private int amount;
        private String interval;
        private boolean sendInvoices;
        private boolean sendSms;
        @Column(name = "plan_currency") // Change the column name
        private String currency;

        // No-arg constructor for JPA
        public Plan() {
        }

        // Constructor for Plan
        public Plan(final ChargeSuccessPayload.DataPayload.Plan plan) {
            if (plan != null) {
                this.id = plan.getId();
                this.name = plan.getName();
                this.planCode = plan.getPlanCode();
                this.description = plan.getDescription();
                this.amount = (plan.getAmount() != null) ? plan.getAmount() : 0; // Ensure no null value
                this.interval = plan.getInterval();
                this.sendInvoices = plan.isSendInvoices();
                this.sendSms = plan.isSendSms();
                this.currency = plan.getCurrency();
            }
        }
    }


    @Embeddable
    @Data
    public static class Log {
        private int timeSpent;
        private int attempts;
        private String authentication;
        private int errors;
        private boolean success;
        private boolean mobile;

        @ElementCollection
        private List<String> input;

        @Column(insertable = false, updatable = false)
        private String channel;

        @ElementCollection
        private List<History> history;

        // No-arg constructor for JPA
        public Log() {
        }

        // Constructor for Log
        public Log(final ChargeSuccessPayload.DataPayload.Log log) {
            if (log != null) {
                this.timeSpent = log.getTimeSpent();
                this.attempts = log.getAttempts();
                this.authentication = log.getAuthentication();
                this.errors = log.getErrors();
                this.success = log.isSuccess();
                this.mobile = log.isMobile();
                this.input = log.getInput();
                this.channel = log.getChannel();
                this.history = log.getHistory().stream()
                        .map(History::new)
                        .collect(Collectors.toList());
            }
        }


        @Embeddable
        @Data
        public static class History {
            private String type;
            private String message;
            private int time;

            // No-arg constructor for JPA
            public History() {
            }

            // Constructor for History
            public History(final ChargeSuccessPayload.DataPayload.Log.History historyItem) {
                this.type = historyItem.getType();
                this.message = historyItem.getMessage();
                this.time = historyItem.getTime();
            }
        }
    }

    @Embeddable
    @Data
    public static class Customer {
        @Column(insertable = false, updatable = false)
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String customerCode;
        private String phone;
        @Column(columnDefinition = "jsonb", insertable = false, updatable = false)
        private String metadata;
        private String riskAction;

        // No-arg constructor for JPA
        public Customer() {
        }

        // Constructor for Customer
        public Customer(final ChargeSuccessPayload.DataPayload.Customer customer) {
            this.id = customer.getId();
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.email = customer.getEmail();
            this.customerCode = customer.getCustomerCode();
            this.phone = customer.getPhone();
            this.metadata = customer.getMetadata().toString();
            this.riskAction = customer.getRiskAction();
        }
    }

    @Embeddable
    @Data
    public static class Authorization {
        private String authorizationCode;
        private String bin;
        private String last4;
        private String expMonth;
        private String expYear;
        private String cardType;
        private String bank;
        private String countryCode;
        private String brand;
        private String accountName;

        // No-arg constructor for JPA
        public Authorization() {
        }

        // Constructor for Authorization
        public Authorization(final ChargeSuccessPayload.DataPayload.Authorization authorization) {
            this.authorizationCode = authorization.getAuthorizationCode();
            this.bin = authorization.getBin();
            this.last4 = authorization.getLast4();
            this.expMonth = authorization.getExpMonth();
            this.expYear = authorization.getExpYear();
            this.cardType = authorization.getCardType();
            this.bank = authorization.getBank();
            this.countryCode = authorization.getCountryCode();
            this.brand = authorization.getBrand();
            this.accountName = authorization.getAccountName();
        }
    }
}

