package com.letsellify.logistics.components.logistics.restController;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.PaystackDataService;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.InitializePaymentDto;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource.PaystackPaymentInitializationResource;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:09:23
 */

@RestController
@RequestMapping("/api/v1/payment/paystack")
@Tag(name = "Paystack API", description = "API for payment collection with paystack payment gateway")
@Slf4j
public class PaystackController {
    private final PaystackDataService paystackDataService;
    private final String paystackSecret;
    private final List<String> paystackAllowedIps;

    public PaystackController(final PaystackDataService paystackDataService, @Value("${paystack.secret-key}") final String paystackSecret, @Value("${paystack.allowed-ips}") final List<String> paystackAllowedIps) {
        this.paystackDataService = paystackDataService;
        this.paystackSecret = paystackSecret;
        this.paystackAllowedIps = paystackAllowedIps;
    }

    @Operation(
      description = "Initialize a transaction",
      summary = "Initialize a transaction to get an access code to complete the payment"
    )
    @PostMapping("/initialize")
    public PaystackPaymentInitializationResource initializePayment(final Authentication authentication, @Valid @RequestBody final InitializePaymentDto initializePaymentDto) {
        return this.paystackDataService.initializePayment(authentication, initializePaymentDto);
    }


    @Hidden
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleChargeSuccessWebhook(final HttpServletRequest request, @RequestHeader("x-paystack-signature") final String signature, @Valid @RequestBody final ChargeSuccessPayload payload) {
        try {
            // Verify IP Address
            final String clientIp = this.getClientIp(request);
            log.info("Received webhook from IP: {}", clientIp);
            if (!this.paystackAllowedIps.contains(clientIp)) {
                log.warn("Unauthorized IP: {}", clientIp);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Verify Signature
            final byte[] requestBody = StreamUtils.copyToByteArray(request.getInputStream());
            final String computedSignature = this.computeHmacSHA512Signature(requestBody, this.paystackSecret);
            if (!computedSignature.equals(signature)) {
                log.warn("Signature mismatch for webhook payload");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Acknowledge the event promptly
            log.info("Valid webhook received, processing payload...");
            Thread.startVirtualThread(() -> this.paystackDataService.handleChargeSuccessWebhook(payload));
            return ResponseEntity.ok().build();

        } catch (final Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private String computeHmacSHA512Signature(final byte[] data, final String secret) throws Exception {
        final Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        final SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        final byte[] macData = sha512Hmac.doFinal(data);
        final StringBuilder result = new StringBuilder();
        for (final byte b : macData) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private String getClientIp(final HttpServletRequest request) {
        final String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null) {
            return forwardedFor.split(",")[0].trim(); // Take the first IP
        }
        return request.getRemoteAddr();
    }

}
