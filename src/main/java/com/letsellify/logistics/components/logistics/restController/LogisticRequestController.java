package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.LogisticRequestDataService;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto.OrderDto;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.FullLogisticRequestResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticRequestResources;
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

import java.util.concurrent.CompletableFuture;

/**
 * Author: Ahmad Buba
 * Date: 8/8/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logistics/")
@Tag(name = "Logistic Request API", description = "API for logistic requests")
public class LogisticRequestController {
    private final LogisticRequestDataService logisticRequestDataService;

    @PostMapping(value = "image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LogisticItemImageResource uploadLogisticsItemImage(final Authentication authentication, @RequestParam final MultipartFile file) {
        System.out.println("Request comes from " + authentication.getName());
        return this.logisticRequestDataService.uploadLogisticItemImage(authentication, file);
    }


    @PostMapping("order-request")
    public CompletableFuture<String> order(final Authentication authentication, @Valid @RequestBody final OrderDto orderDto) {
        return this.logisticRequestDataService.order(authentication, orderDto);
    }

    @GetMapping("order-request/{id}")
    public FullLogisticRequestResource getLogisticRequest(final Authentication authentication, @PathVariable("id") final String logisticRequestId) {
        return this.logisticRequestDataService.getLogisticRequest(authentication, logisticRequestId);
    }

    @GetMapping("order-request")
    public LogisticRequestResources getLogisticRequests(
            final Authentication authentication,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "5") final int size,
            @RequestParam(defaultValue = "creationDate") final String sortBy,
            @RequestParam(defaultValue = "true") final boolean descending
    ) {
        final Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);
        return this.logisticRequestDataService.getLogisticRequests(authentication, pageable);
    }

//    @GetMapping("logistics/order-request")
//    public LogisticRequestResources getLogisticRequests(
//            final Authentication authentication,
//            @RequestParam(defaultValue = "0") final int page,
//            @RequestParam(defaultValue = "5") final int size,
//            @RequestParam(defaultValue = "creationDate") final String sortBy,
//            @RequestParam(defaultValue = "true") final boolean descending
//    ) {
//        final Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
//        final Pageable pageable = PageRequest.of(page, size, sort);
//        return logisticRequestDataService.getLogisticRequests(authentication, pageable);
//    }
}
