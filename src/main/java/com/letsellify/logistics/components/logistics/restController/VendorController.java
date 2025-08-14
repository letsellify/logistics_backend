package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.vendorManagement.VendorDataService;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.VendorProfileInfoDto;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.VendorProfilePictureDto;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource.VendorProfileInfoResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author AHMAD BUBA
 * Date:2/18/25
 * Time:04:14
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vendor")
@Tag(
        name = "Vendor API",
        description = "APIs for vendor operations, including initial profile creation, profile picture uploads, and other vendor-related functionalities."
)
public class VendorController {
    private final VendorDataService vendorDataService;

    @Operation(
            summary = "Create vendor profile",
            description = """
                        Creates the authenticated vendor's profile. 
                        This action can only be done once before posting any vendor content. 
                        To make changes after posting, contact the administrator.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Vendor profile information",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VendorProfileInfoDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VendorProfileInfoResource.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid profile data"),
                    @ApiResponse(responseCode = "403", description = "Profile cannot be updated after posting"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            }
    )
    @PostMapping("/profile")
    public VendorProfileInfoResource setProfile(
            final Authentication authentication,
            @Valid @RequestBody final VendorProfileInfoDto vendorProfileInfoDto
    ) {
        return this.vendorDataService.setProfile(authentication, vendorProfileInfoDto);
    }

    @Operation(
            summary = "Get vendor profile",
            description = "Retrieves the authenticated vendor's profile information.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VendorProfileInfoResource.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated"),
                    @ApiResponse(responseCode = "404", description = "Profile not found")
            }
    )
    @GetMapping("/profile")
    public VendorProfileInfoResource getProfile(final Authentication authentication) {
        return this.vendorDataService.getProfile(authentication);
    }

    @Operation(
            summary = "Upload vendor profile picture",
            description = """
                        Uploads a profile picture for the authenticated vendor.
                        Can only be done before posting any vendor content.
                        To change the profile picture after posting, contact the administrator.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile picture file to upload",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = VendorProfilePictureDto.class)
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
                                            example = "https://cdn.example.com/vendor/profile-picture.jpg"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid file format or size"),
                    @ApiResponse(responseCode = "403", description = "Profile picture cannot be updated after posting"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
            }
    )
    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadProfilePicture(
            final Authentication authentication,
            @ModelAttribute VendorProfilePictureDto dto
    ) {
        return this.vendorDataService.uploadProfilePicture(authentication, dto.file());
    }

}

