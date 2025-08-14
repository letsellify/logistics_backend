package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.financeAccountManagement.FinanceAccountDataService;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.rest.dto.TopUpAccountDto;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackInitiateTransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/finance")
@Tag(name = "Finance API", description = "API for managing finance accounts and top-ups")
public class FinanceAccountController {

    private final FinanceAccountDataService financeAccountDataService;

    @Operation(
            summary = "Get finance account balance",
            description = "Retrieves the current balance for the authenticated user's finance account.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Balance retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "number", format = "bigdecimal", example = "1250.75")
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            }
    )
    @GetMapping("/account")
    public BigDecimal getFinanceAccountBalance(final Authentication authentication) {
        return this.financeAccountDataService.getFinanceAccountBalance(authentication);
    }

    @Operation(
            summary = "Initialize account top-up",
            description = """
            Initiates a top-up transaction for the authenticated user's finance account.
            The amount to top up is specified in the request body.
            This endpoint communicates with Paystack's payment gateway to generate a unique payment URL,
            which the user can use to complete the transaction.
        """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Top-up details including the amount to top up",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TopUpAccountDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Top-up initialized successfully. Payment URL generated.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaystackInitiateTransactionResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            }
    )
    @PostMapping("/account")
    public PaystackInitiateTransactionResponse initializeTopUp(
            final Authentication authentication,
            @Valid @RequestBody final @NonNull TopUpAccountDto topUpAccountDto
    ) {
        return this.financeAccountDataService.initializeTopUp(authentication, topUpAccountDto);
    }
}

