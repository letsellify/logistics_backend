package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.AccountManager;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.data.LogisticsAccountTransaction;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.data.Payment;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.dataMapper.PaystackDataMapper;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity.PaystackChargeSuccessWebhookEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity.PaystackPaymentEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.repository.PaystackPaymentRepository;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.PaystackInitiateTransactionRequest;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:06
 */

@Component
@Slf4j
public class PaystackManager {
    private final PaystackPaymentRepository paymentRepository;
    // this will go soon. another bounded context
    private final AccountManager accountManager;
    private final RestClient restClient;
    private final String paystackSecret;
    private final List<String> paystackAllowedIps;

    public PaystackManager(
      final PaystackPaymentRepository paystackPaymentRepository,
      final UserManager userManager,
      final AccountManager accountManager,
      @Qualifier("PaystackRestClient") final RestClient restClient,
      @Value("${paystack.secret-key}") final String paystackSecret,
      @Value("${paystack.allowed-ips}") final List<String> paystackAllowedIps
    ) {
        this.paymentRepository = paystackPaymentRepository;
        this.accountManager = accountManager;
        this.restClient = restClient;
        this.paystackSecret = paystackSecret;
        this.paystackAllowedIps = paystackAllowedIps;
    }

    // possibly do a validation to check that the provided string is all digit: amount(String)
    @Transactional
    public Payment initializePayment(final @NonNull String email, final @NonNull LogisticsAppRole userRole, final @NonNull BigDecimal amount) throws UserNotFoundException {
       final PaystackPaymentEntity entity = PaystackPaymentEntity.getInstance(email, userRole, amount);
       final PaystackInitiateTransactionRequest requestBody = new PaystackInitiateTransactionRequest(email, amount.toString());
       final PaystackInitiateTransactionResponse responseBody = this.restClient.post()
                                                                               .uri("/transaction/initialize")
                                                                               .body(requestBody)
                                                                               .retrieve()
                                                                               .body(PaystackInitiateTransactionResponse.class);
       assert responseBody != null;
       entity.setInitializationStatus(responseBody.isStatus());
       entity.setInitializationMessage(responseBody.getMessage());
       entity.setAuthorizationUrl(responseBody.getData().getAuthorizationUrl());
       entity.setAccessCode(responseBody.getData().getAccessCode());
       entity.setReference(responseBody.getData().getReference());
       this.paymentRepository.save(entity);
       return new Payment(entity);
    }


    // verify transaction method: updates a payment based on webhook data
    // add logging here
    @Transactional
    public void handleChargeSuccessWebhook(final @NonNull ChargeSuccessPayload payload) {
        final PaystackChargeSuccessWebhookEntity chargeSuccessWebhookEntity = PaystackDataMapper.INSTANCE.resourceToEntity(payload);
        final PaystackPaymentEntity paymentEntity = this.paymentRepository.findByAccessCode(payload.getData().getCustomer().getCustomerCode())
                                                                          .orElseThrow();
        paymentEntity.setChargeSuccessWebhookData(chargeSuccessWebhookEntity);
        paymentEntity.setSuccess(true);
        this.paymentRepository.save(paymentEntity);
        try {
            // not needed, make sure to check paystack docs again, seems it's returned as string
            // if so we convert string to bigDecimal
            final BigDecimal topUpAmount = BigDecimal.valueOf(payload.getData().getAmount()).setScale(2, RoundingMode.HALF_UP);
            // put it on a kafka, accountManager listens
            final LogisticsAccountTransaction accountTransaction = this.accountManager.topUpAccount(paymentEntity.getUserEmail(), paymentEntity.getUserRole(), topUpAmount);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void validateConfig() {
        if (this.paystackSecret == null || this.paystackSecret.isBlank()) {
            throw new IllegalStateException("Paystack secret key is not configured!");
        }
        if (this.paystackAllowedIps.isEmpty()) {
            throw new IllegalStateException("Paystack allowed IPs are not configured!");
        }
    }
}
