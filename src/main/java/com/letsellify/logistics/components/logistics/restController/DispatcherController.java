package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfilePictureDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.VendorProfilePictureDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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


    @Operation(
            summary = "Upload Dispatcher profile picture",
            description = """
                        Uploads a profile picture for the authenticated dispatcher.
                        Can only be done before beginning to accept logistic requests.
                        To change the profile picture after posting, contact the administrator.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile picture file to upload",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = DispatcherProfilePictureDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile picture uploaded successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "string",
                                            example = "https://cdn.example.com/dispatcher/profile-picture.jpg"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid file format or size"),
                    @ApiResponse(responseCode = "403", description = "Profile picture cannot be updated after posting"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            }
    )
    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadProfilePhoto(final Authentication authentication, final @ModelAttribute DispatcherProfilePictureDto profilePictureDto) {
        return this.dispatcherDataService.uploadProfilePhoto(authentication, profilePictureDto.file());
    }
}
