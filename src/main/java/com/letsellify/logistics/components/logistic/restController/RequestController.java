package com.letsellify.logistics.components.logistic.restController;


import com.letsellify.logistics.components.logistic.core.request.LogisticRequestDataService;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticRequestResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
