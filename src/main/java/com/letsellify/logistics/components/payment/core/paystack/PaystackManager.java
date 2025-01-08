package com.letsellify.logistics.components.payment.core.paystack;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.letsellify.logistics.components.payment.core.paystack.config.PaystackConfig;
import com.letsellify.logistics.components.payment.core.paystack.data.Payment;
import com.letsellify.logistics.components.payment.core.paystack.database.entity.PaystackPaymentEntity;
import com.letsellify.logistics.components.payment.core.paystack.database.repository.PaystackPaymentRepository;
import com.letsellify.logistics.components.payment.core.paystack.rest.dto.PaystackInitiateTransactionDto;
import com.letsellify.logistics.components.payment.core.paystack.rest.resource.PaystackTransactionResponse;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:16:06
 */

@Component
@Slf4j
@EnableConfigurationProperties(PaystackConfig.class)
public class PaystackManager {
    private final PaystackPaymentRepository repository;
    private final UserManager userManager;
    private final RestClient restClient;

    public PaystackManager(final PaystackPaymentRepository paystackPaymentRepository, final UserManager userManager, @Qualifier("PaystackRestClient") final RestClient restClient) {
        this.repository = paystackPaymentRepository;
        this.userManager = userManager;
        this.restClient = restClient;
    }

    // possibly do a validation to check that the provided string is all digit: amount(String)
    public Payment initializePayment(@NonNull final String email, @NonNull final String amount) throws UserNotFoundException {
       final LogisticsAppUser appUser = this.userManager.getUserByEmail(email);
       final BigDecimal actualAmount = new BigDecimal(amount);
       final PaystackPaymentEntity entity = PaystackPaymentEntity.getInstance(actualAmount, appUser.getId());
       final PaystackInitiateTransactionDto requestBody = new PaystackInitiateTransactionDto(email, amount);
       final PaystackTransactionResponse responseBody = this.restClient.post()
                                                                        .uri("/transaction/initialize")
                                                                        .body(requestBody)
                                                                        .retrieve()
                                                                        .body(PaystackTransactionResponse.class);
       assert responseBody != null;
       entity.setInitializationStatus(responseBody.isStatus());
       entity.setInitializationMessage(responseBody.getMessage());
       entity.setAuthorizationUrl(responseBody.getData().getAuthorizationUrl());
       entity.setAccessCode(responseBody.getData().getAccessCode());
       entity.setReference(responseBody.getData().getReference());
       this.repository.save(entity);
       return new Payment(entity);
    }

    // verify transaction method: updates a payment based on webhook data
}
