package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.PaystackDataService;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto.ChargeSuccessPayload;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AHMAD BUBA
 * Date:2/18/25
 * Time:10:57
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paystack")
@Hidden
public class PaystackController {
    private final PaystackDataService paystackDataService;

    @PostMapping("")
    public ResponseEntity<String> handleChargeSuccessWebhook(
            final @Valid @RequestBody ChargeSuccessPayload webhookData
    ) {
        this.paystackDataService.handleChargeSuccessWebhook(webhookData);
        return ResponseEntity.status(HttpStatus.OK).body("Webhook received successfully");
    }
}
