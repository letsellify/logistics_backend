package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherNotificationDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfilePictureDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.LgaPreferenceDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResources;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.LgaResource;
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

import java.util.UUID;

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


    @Operation(
            summary = "Add dispatcher notification preference",
            description = """
                    Adds a new LGA pair preference for the authenticated dispatcher. 
                    Each preference specifies a pick-up LGA and a drop-off LGA. 
                    When preferences exist, the dispatcher will only receive notifications for those specific LGA pairs. 
                    By default, if no preferences are set, the dispatcher receives all notifications.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The LGA pair to add as a preference",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LgaPreferenceDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preference added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DispatcherLgaPreferenceResource.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/notification-preferences")
    public DispatcherLgaPreferenceResource addLgaPreference(final Authentication authentication, @RequestBody final @Valid LgaPreferenceDto lgaPreferenceDto) {
        return this.dispatcherDataService.addLgaPreference(authentication,lgaPreferenceDto);
    }

    @Operation(
            summary = "Get dispatcher notification preferences",
            description = """
                    Retrieves the list of notification preferences (LGA pairs) for the authenticated dispatcher.
                    If no preferences are set, the dispatcher receives all notifications by default.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Preferences retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DispatcherLgaPreferenceResources.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/notification-preferences")
    public DispatcherLgaPreferenceResources getLgaPreferences(final Authentication authentication) {
        return this.dispatcherDataService.getLgaPreferences(authentication);
    }

    @Operation(
            summary = "Update dispatcher notification mode",
            description = """
                    Updates the notification mode for the dispatcher.
                    - If `all = true`, clears all preferences and the dispatcher will receive all notifications.
                    - If `all = false`, dispatcher will only receive notifications for the LGA pairs they have set.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Notification mode (all or preferences)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DispatcherNotificationDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Notification mode updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/notification-preferences")
    public void updateNotificationPreference(final Authentication authentication, @RequestBody final @Valid DispatcherNotificationDto dispatcherNotificationDto) {
        this.dispatcherDataService.updateNotificationPreference(authentication, dispatcherNotificationDto);
    }

    @Operation(
            summary = "Delete dispatcher notification preference",
            description = """
                    Deletes a specific notification preference by its ID.
                    If the dispatcher has no preferences left, they will receive all notifications by default.
                    """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Preference deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated"),
                    @ApiResponse(responseCode = "404", description = "Preference not found")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/notification-preferences/{id}")
    public void deleteNotificationPreference(final Authentication authentication, @PathVariable("id") final UUID preferenceId) {
        this.dispatcherDataService.deleteNotificationPreference(authentication, preferenceId);
    }
}
