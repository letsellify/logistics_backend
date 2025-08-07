package com.letsellify.logistics.components.logistic.restController;

import com.letsellify.logistics.components.logistic.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.VendorLogisticRequestResource;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.VendorLogisticRequestResources;
import com.letsellify.logistics.components.logistic.core.vendor.VendorDataService;
import com.letsellify.logistics.components.logistic.core.vendor.rest.dto.*;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorBusinessInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorContactInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorPersonalInformationResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @PostMapping("/personal-info")
    public VendorPersonalInformationResource uploadPersonalInformation(final Authentication authentication, @Valid @RequestBody final PersonalInformationDto personalInformationDto) {
        return this.vendorDataService.uploadPersonalInformation(authentication, personalInformationDto);
    }

    @PostMapping("/contact-info")
    public VendorContactInformationResource uploadContactInformation(final Authentication authentication, @Valid @RequestBody final ContactInformationDto contactInformationDto) {
        return this.vendorDataService.uploadContactInformation(authentication, contactInformationDto);
    }

    @PostMapping("/business-info")
    public VendorBusinessInformationResource uploadBusinessInformation(final Authentication authentication, @Valid @RequestBody final BusinessInformationDto businessInformationDto) {
        return this.vendorDataService.uploadBusinessInformation(authentication, businessInformationDto);
    }

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadProfilePicture(final Authentication authentication, @RequestParam("file") MultipartFile file) {
        return this.vendorDataService.uploadProfilePicture(authentication, file);
    }

    @GetMapping("/info")
    public VendorInformationResource getInformation(final Authentication authentication) {
        return this.vendorDataService.getVendorInformation(authentication);
    }

    @PostMapping("/account/initialize-topUp")
    public PaystackInitiateTransactionResponse initializeTopUp(
            final Authentication authentication,
            @Valid @RequestBody final VendorTopUpDto topUpDto
    ) {
        return this.vendorDataService.initializeTopUp(authentication, topUpDto);
    }

    @PostMapping(value = "logistics/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LogisticItemImageResource uploadLogisticsItemImage(final Authentication authentication, @RequestParam final MultipartFile file) {
        System.out.println("Request comes from " + authentication.getName());
        return this.vendorDataService.uploadLogisticsItemImage(authentication, file);
    }


    @PostMapping("logistics/order-request")
    public CompletableFuture<String> order(final Authentication authentication, @Valid @RequestBody final OrderDto orderDto) {
        return this.vendorDataService.order(authentication, orderDto);
    }

//    @GetMapping("logistics/order-request")
//    public VendorLogisticRequestResources getLogisticRequests(Authentication authentication) {
//        return this.vendorDataService.getLogisticRequests(authentication);
//    }

    @GetMapping("logistics/order-request/{id}")
    public VendorLogisticRequestResource getLogisticRequest(final Authentication authentication, @PathVariable("id") final String logisticRequestId) {
        return this.vendorDataService.getLogisticRequest(authentication, logisticRequestId);
    }

    @GetMapping("logistics/order-request")
    public VendorLogisticRequestResources getLogisticRequests(
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "5") final int size,
            @RequestParam(defaultValue = "creationDate") final String sortBy,
            @RequestParam(defaultValue = "true") final boolean descending
    ) {
        final Sort sort = descending? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);
        return this.vendorDataService.getLogisticRequests(authentication, pageable);
    }

}
