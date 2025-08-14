package com.letsellify.logistics.components.user.restController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.user.core.userManagement.UserDataService;
import com.letsellify.logistics.components.user.core.userManagement.rest.dto.CreateUserDto;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(
        name = "User API",
        description = "Endpoints for user registration and profile information"
)
@Slf4j
public class UserController {

    private final UserDataService userDataService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided details and returns the details of the created user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateUserDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example User Registration",
                                            value = """
                        {
                     
                          "email": "john.doe@example.com",
                          "password": "P@ssw0rd",
                          "role": "AGENT"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(
                                    schema = @Schema(implementation = UserResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data or validation error"
                    )
            }
    )
    @PostMapping("/register")
    public UserResource createUser(
            @Valid @RequestBody final CreateUserDto createUserDto
    ) {
        return this.userDataService.createUser(createUserDto);
    }

    @Operation(
            summary = "Get authenticated user details",
            description = "Returns user details for the currently authenticated user.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved user details",
                            content = @Content(
                                    schema = @Schema(implementation = UserResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User not authenticated"
                    )
            }
    )
    @GetMapping("/me")
    public UserResource getMyself(
            final Authentication authentication,
            final HttpServletRequest request
    ) {
        log.info("User details requested for: {}", authentication.getName());
        log.info("Domain: {}", request.getServerName());
        return this.userDataService.getUser(authentication.getName());
    }
}
