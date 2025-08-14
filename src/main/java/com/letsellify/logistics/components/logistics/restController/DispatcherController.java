package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:03:45
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dispatcher")
@Tag(
        name = "Dispatchers API",
        description = """
        API endpoints for managing dispatcher profiles.
        You must be authenticated to use these endpoints.
        Once you have posted content as a dispatcher, your profile and profile picture cannot be updated without notifying an administrator.
        """
)
public class DispatcherController {

    private final DispatcherDataService dispatcherDataService;

    @Operation(
            summary = "Set dispatcher profile",
            description = """
            Creates or updates the dispatcher profile for the authenticated user.
            Once you have posted content, profile updates (including profile picture changes) must be approved by an administrator.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile successfully created or updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DispatcherProfileInfoResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/profile")
    public DispatcherProfileInfoResource setProfile(
            final @NonNull Authentication authentication,
            @Valid @RequestBody final DispatcherProfileDto infoDto
    ) {
        return this.dispatcherDataService.setProfile(authentication, infoDto);
    }

    @Operation(
            summary = "Get dispatcher profile",
            description = "Retrieves the dispatcher profile for the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dispatcher profile retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DispatcherProfileInfoResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Profile not found"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/profile")
    public DispatcherProfileInfoResource getProfile(
            final @NonNull Authentication authentication
    ) {
        return this.dispatcherDataService.getProfile(authentication);
    }
}
