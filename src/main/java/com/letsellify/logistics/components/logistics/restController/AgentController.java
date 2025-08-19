package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.agentManagement.AgentDataService;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentProfileDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentProfilePictureDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfilePictureDto;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:14:02
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
@Tag(
        name = "Agents API",
        description = """
                API endpoints for managing agent profiles.
                You must be authenticated to use these endpoints.
                Once you have posted content as a agent, your profile and profile picture cannot be updated without notifying an administrator.
                """
)
public class AgentController {
    private final AgentDataService agentDataService;

    @Operation(
            summary = "Set agent profile",
            description = """
                    Creates or updates the agent profile for the authenticated user.
                    Once you have posted content, profile updates (including profile picture changes) must be approved by an administrator.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile successfully created or updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AgentProfileInfoResource.class)
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
    public AgentProfileInfoResource setProfile(
            final  Authentication authentication,
            @Valid @RequestBody final AgentProfileDto infoDto
    ) {
        return this.agentDataService.setProfile(authentication, infoDto);
    }

    @Operation(
            summary = "Get agent profile",
            description = "Retrieves the agent profile for the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Agent profile retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AgentProfileInfoResource.class)
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
    public AgentProfileInfoResource getProfile(
            final Authentication authentication
    ) {
        return this.agentDataService.getProfile(authentication);
    }


    @Operation(
            summary = "Upload Agent profile picture",
            description = """
                        Uploads a profile picture for the authenticated agent.
                        Can only be done before beginning to accept logistic requests.
                        To change the profile picture after posting, contact the administrator.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile picture file to upload",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = AgentProfilePictureDto.class)
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
    public String uploadProfilePhoto(final Authentication authentication, final @ModelAttribute AgentProfilePictureDto profilePictureDto) {
        return this.agentDataService.uploadProfilePhoto(authentication, profilePictureDto.file());
    }
}
