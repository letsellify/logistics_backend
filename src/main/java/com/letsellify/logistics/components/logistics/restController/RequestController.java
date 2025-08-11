package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.LogisticRequestDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Ahmad Buba
 * Date: 8/6/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-request")
@Tag(name = "Logistic Request API", description = "API's for managing logistic requests")
public class RequestController {
    private final LogisticRequestDataService logisticRequestDataService;

//    @GetMapping
//    public LogisticRequestResource getLogisticRequests(final Authentication authentication) {
//        return this.logisticRequestDataService.getLogisticRequests(authentication);
//    }
}
