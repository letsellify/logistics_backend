package com.letsellify.logistics.components.logistic.restController;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistic.core.vendor.VendorDataService;
import com.letsellify.logistics.components.logistic.core.vendor.rest.dto.OrderDto;
import com.letsellify.logistics.components.logistic.core.vendor.rest.dto.VendorTopUpDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/18/25
 * Time:04:14
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vendor")
@Tag(name = "Vendor API", description = "API's vendors")
public class VendorController {
    private final VendorDataService vendorDataService;

    @PostMapping("/account/initialize-topUp")
    public PaystackInitiateTransactionResponse initializeTopUp(
      final Authentication authentication,
      @Valid @RequestBody final VendorTopUpDto topUpDto
    ) {
        return this.vendorDataService.initializeTopUp(authentication,topUpDto);
    }

    @PostMapping(value = "logistics/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LogisticItemImageResource uploadLogisticsItemImage(final Authentication authentication, @RequestParam final MultipartFile file) {
        return this.vendorDataService.uploadLogisticsItemImage(authentication,file);
    }


    @PostMapping("logistics/order-request")
    public CompletableFuture<String> order(final Authentication authentication, @Valid @RequestBody final OrderDto orderDto) {
        return this.vendorDataService.order(authentication,orderDto);
    }
}
