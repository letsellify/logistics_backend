package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.ItemConditionDataService;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemConditionResources;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item-condition")
@Tag(
        name = "Item Condition API",
        description = "API for retrieving available item conditions when making a logistic request. "
                + "You must be authenticated to use this endpoint. "
                + "If you send an unknown condition, it will be automatically added to our list."
)
public class ItemConditionController {

    private final ItemConditionDataService itemConditionDataService;

    @Operation(
            summary = "Get item conditions",
            description = """
            Retrieves all predefined item conditions for logistic requests.
            If you specify a condition that does not exist, it will be added automatically.
            Authentication is required.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of available item conditions",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LogisticItemConditionResources.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public LogisticItemConditionResources getItemConditions() {
        return this.itemConditionDataService.getItemConditions();
    }
}

