package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.LogisticRequestDataService;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto.LogisticImageDto;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto.OrderDto;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.FullLogisticRequestResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticRequestResources;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Upload an image for a logistic item",
            description = "Uploads an image file for a logistic item.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = LogisticImageDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image uploaded successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LogisticItemImageResource.class))
                    )
            }
    )
    @PostMapping(value = "image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LogisticItemImageResource uploadLogisticsItemImage(
            final Authentication authentication,
            @ModelAttribute LogisticImageDto logisticImageDto
            ) {
        System.out.println("Request comes from " + authentication.getName());
        return this.logisticRequestDataService.uploadLogisticItemImage(authentication, logisticImageDto.file());
    }


    @Operation(
            summary = "Place a new logistic order",
            description = "Creates a new logistic order for the authenticated vendor.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order placed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @PostMapping("order-request")
    public CompletableFuture<String> order(final Authentication authentication, @Valid @RequestBody final OrderDto orderDto) {
        return this.logisticRequestDataService.order(authentication, orderDto);
    }

    @Operation(
            summary = "Get details of a logistic request",
            description = "Retrieves the details of a specific logistic request by its ID.",
            parameters = {
                    @Parameter(name = "id", description = "The ID of the logistic request", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logistic request found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FullLogisticRequestResource.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Logistic request not found")
            }
    )
    @GetMapping("order-request/{id}")
    public FullLogisticRequestResource getLogisticRequest(final Authentication authentication, @PathVariable("id") final String logisticRequestId) {
        return this.logisticRequestDataService.getLogisticRequest(authentication, logisticRequestId);
    }


    @Operation(
            summary = "List logistic requests",
            description = "Retrieves a paginated list of logistic requests for the authenticated user.",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Number of records per page", example = "5"),
                    @Parameter(name = "sortBy", description = "Field to sort by", example = "creationDate"),
                    @Parameter(name = "descending", description = "Sort order descending if true", example = "true")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of logistic requests retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LogisticRequestResources.class)
                            )
                    )
            }
    )
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
