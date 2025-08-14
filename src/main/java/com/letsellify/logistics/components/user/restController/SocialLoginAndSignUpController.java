package com.letsellify.logistics.components.user.restController;

import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.SocialLoginAndSignUpDataService;
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.rest.dto.RoleSelectionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:21:38
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-signup")
@Tag(name = "Social Signup API", description = "API to Complete Social Signup")
public class SocialLoginAndSignUpController {
    private final SocialLoginAndSignUpDataService socialLoginAndSignUpDataService;


    @Operation(
            summary = "Select role to complete social signup",
            description = """
                        Completes the registration process for users who signed up via a social login provider 
                        (e.g., Google, Facebook).  
                        After social authentication, the user must choose a role to finalize account creation.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Role selection payload containing the temporary signup token and chosen role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoleSelectionDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role assigned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = """
                                                        {
                                                          "message": "Role Assigned"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid token or role selection"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - social login token missing or expired")
            }
    )
    @PostMapping("/role-selection")
    public ResponseEntity<?> assignRole(
            final HttpServletResponse httpServletResponse,
            @RequestBody final @Valid RoleSelectionDto roleSelectionDto
    ) {
        this.socialLoginAndSignUpDataService.assignRole(
                httpServletResponse,
                roleSelectionDto.token(),
                roleSelectionDto.role()
        );
        return ResponseEntity.ok(Map.of("message", "Role Assigned"));
    }
}
