package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.FinanceAccountManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity.PaystackChargeSuccessWebhookEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity.PaystackPaymentEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.repository.PaystackPaymentRepository;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.exception.PaystackWebhookException;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto.PaystackInitiateTransactionRequest;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackListBanksResponse;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackResolveBankAccountResponse;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:06
 */

@Component
@Slf4j
public class PaystackManager {
    private final PaystackPaymentRepository paystackPaymentRepository;
    private final FinanceAccountManager accountManager;   // this will go soon. another bounded context
    private final RestClient restClient;
    private final String paystackSecret;
    private final List<String> paystackAllowedIps;

    public PaystackManager(
            final PaystackPaymentRepository paystackPaymentRepository,
            final FinanceAccountManager accountManager,
            @Qualifier("PaystackRestClient") final RestClient restClient,
            @Value("${paystack.secret-key}") final String paystackSecret,
            @Value("${paystack.allowed-ips}") final List<String> paystackAllowedIps
    ) {
        this.paystackPaymentRepository = paystackPaymentRepository;
        this.accountManager = accountManager;
        this.restClient = restClient;
        this.paystackSecret = paystackSecret;
        this.paystackAllowedIps = paystackAllowedIps;
    }


    @PostConstruct
    public void init() {
        if (this.paystackSecret == null || this.paystackSecret.isBlank()) {
            throw new IllegalStateException("Paystack secret key is not configured!");
        }
        if (this.paystackAllowedIps.isEmpty()) {
            throw new IllegalStateException("Paystack allowed IPs are not configured!");
        }
    }

    // possibly do a validation to check that the provided string is all digit: amount(String)
    @Transactional
    public PaystackInitiateTransactionResponse initializePayment(final @NonNull UUID userId, final @NonNull String userEmail, @NonNull LogisticAppRole userRole, final @NonNull BigDecimal amount) throws UserNotFoundException {
        final String totalAmount = amount.toString();
        log.info("Original amount received from frontend {}", totalAmount);
        final String totalAmountinKobo = amount.multiply(BigDecimal.valueOf(100)).toPlainString();
        final PaystackInitiateTransactionRequest requestBody = new PaystackInitiateTransactionRequest(userEmail, totalAmountinKobo);
        final PaystackInitiateTransactionResponse responseBody = this.restClient.post()
                .uri("/transaction/initialize")
                .body(requestBody)
                .retrieve()
                .body(PaystackInitiateTransactionResponse.class);
        assert responseBody != null;
        final PaystackPaymentEntity entity = PaystackPaymentEntity.create(
                userId,
                userRole,
                amount,
                responseBody.isStatus(),
                responseBody.getMessage(),
                responseBody.getData().getAuthorizationUrl(),
                responseBody.getData().getAccessCode(),
                responseBody.getData().getReference()
        );
        this.paystackPaymentRepository.save(entity);
        return responseBody;
    }


    // verify transaction method: updates a payment based on webhook data
    // add logging here
    @Transactional
    public void handleChargeSuccessWebhook(final @NonNull ChargeSuccessPayload payload) throws PaystackWebhookException {
        log.info("Webhook received from paystack");
        final PaystackChargeSuccessWebhookEntity chargeSuccessWebhookEntity = new PaystackChargeSuccessWebhookEntity(payload);
        final PaystackPaymentEntity paymentEntity = this.paystackPaymentRepository.findByReference(payload.getData().getReference())
                .orElseThrow(() -> new PaystackWebhookException("no record found for reference " + payload.getData().getReference()));
        paymentEntity.transactionSuccess(chargeSuccessWebhookEntity);
        this.paystackPaymentRepository.save(paymentEntity);
        log.info("Payment status is {}, with the following data {}", paymentEntity.isSuccess(), paymentEntity.getChargeSuccessWebhookData().toString());
        try {
            // put it on a kafka, accountManager listens
            this.accountManager.topUpAccount(paymentEntity.getUserId(), paymentEntity.getUserRole(), paymentEntity.getAmount());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }


    public PaystackListBanksResponse getBanks() {
        final PaystackListBanksResponse response = this.restClient.get()
                .uri(uriBuilder -> uriBuilder.path("bank")
                        .queryParam("currency", "NGN")
                        .build())
                .retrieve()
                .body(PaystackListBanksResponse.class);
        log.info("Retrieved banks: {}", response);
        return response;
    }

//    public PaystackResolveBankAccountResponse resolveAccountNumber(final String accountNumber, final String bankCode) {
//        final PaystackResolveBankAccountResponse response = this.restClient.get()
//                                                              .uri(uriBuilder -> uriBuilder.path("resolve")
//                                                                                           .queryParam("account_number", accountNumber)
//                                                                                           .queryParam("bank_code", bankCode)
//                                                                                           .build())
//                                                              .retrieve()
//                                                              .body(PaystackResolveBankAccountResponse.class);
//        log.info("Resolved account: {}", response);
//        return response;
//
//    }

    public PaystackResolveBankAccountResponse resolveAccountNumber(final String accountNumber, final String bankCode) {
        final String requestUrl = UriComponentsBuilder.fromPath("resolve")
                .queryParam("account_number", accountNumber)
                .queryParam("bank_code", bankCode)
                .toUriString();

        log.info("Sending request to Paystack: {}", requestUrl);

        // Get raw response as String
        final String rawResponse = this.restClient.get()
                .uri(uriBuilder -> uriBuilder.path("resolve")
                        .queryParam("account_number", accountNumber)
                        .queryParam("bank_code", bankCode)
                        .build())
                .retrieve()
                .body(String.class);  // Get response as plain string

        log.info("Raw JSON response from Paystack: {}", rawResponse);

        // Convert raw JSON to object
        PaystackResolveBankAccountResponse response = null;
        try {
            response = new ObjectMapper().readValue(rawResponse, PaystackResolveBankAccountResponse.class);
            log.info("Mapped response: {}", response);
        } catch (Exception e) {
            log.error("Error mapping Paystack response: ", e);
        }

        return response;
    }


}
