package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.letsellify.logistics.components.logistics.core.financeAccount.AccountManager;
import com.letsellify.logistics.components.logistics.core.financeAccount.data.LogisticsAccount;
import com.letsellify.logistics.components.logistics.core.money.MoneyManager;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.data.Payment;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.dataMapper.PaystackDataMapper;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity.PaystackChargeSuccessWebhookEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity.PaystackPaymentEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.repository.PaystackPaymentRepository;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.PaystackInitiateTransactionRequest;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistics.core.user.UserManager;
import com.letsellify.logistics.components.logistics.core.user.data.LogisticsAppUser;
import com.letsellify.logistics.components.logistics.core.user.exception.UserNotFoundException;

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
    private final UserManager userManager;
    private final AccountManager accountManager;
    private final MoneyManager moneyManager;
    private final RestClient restClient;
    private final String paystackSecret;
    private final List<String> paystackAllowedIps;

    public PaystackManager(
      final PaystackPaymentRepository paystackPaymentRepository,
      final UserManager userManager,
      final AccountManager accountManager,
      final MoneyManager moneyManager,
      @Qualifier("PaystackRestClient") final RestClient restClient,
      @Value("${paystack.secret-key}") final String paystackSecret,
      @Value("${paystack.allowed-ips}") final List<String> paystackAllowedIps
    ) {
        this.paymentRepository = paystackPaymentRepository;
        this.userManager = userManager;
        this.accountManager = accountManager;
        this.moneyManager = moneyManager;
        this.restClient = restClient;
        this.paystackSecret = paystackSecret;
        this.paystackAllowedIps = paystackAllowedIps;
    }

    // possibly do a validation to check that the provided string is all digit: amount(String)
    @Transactional
    public Payment initializePayment(final @NonNull String email, final @NonNull String amount) throws UserNotFoundException {
       final LogisticsAppUser appUser = this.userManager.getUserByEmail(email);
       final BigDecimal actualAmount = new BigDecimal(amount);
       final PaystackPaymentEntity entity = PaystackPaymentEntity.getInstance(actualAmount, appUser.getId());
       final PaystackInitiateTransactionRequest requestBody = new PaystackInitiateTransactionRequest(email, amount);
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
    @Transactional
    public void handleChargeSuccessWebhook(final @NonNull ChargeSuccessPayload payload) {
        final LogisticsAccount account;
        final PaystackChargeSuccessWebhookEntity chargeSuccessWebhookEntity = PaystackDataMapper.INSTANCE.resourceToEntity(payload);
        final PaystackPaymentEntity paymentEntity = this.paymentRepository.findByAccessCode(payload.getData().getCustomer().getCustomerCode())
                                                                          .orElseThrow();
        paymentEntity.setChargeSuccessWebhookData(chargeSuccessWebhookEntity);
        paymentEntity.setSuccess(true);
        this.paymentRepository.save(paymentEntity);
        try {
            final BigDecimal topUpAmount = this.moneyManager.convertAmount(payload.getData()
                                                                                  .getAmount());
            account = this.accountManager.topUpAccount(paymentEntity.getUserId(), topUpAmount);
        }
        catch (Exception e) {
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
