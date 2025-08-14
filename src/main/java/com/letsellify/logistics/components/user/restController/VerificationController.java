package com.letsellify.logistics.components.user.restController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.user.core.verificationCodeManagement.VerificationCodeDataService;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto.ResendVerificationCodeDto;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto.VerificationCodeDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:17:01
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verification")
@Tag(
        name = "Verification API",
        description = "Endpoints for email verification during user registration"
)
public class VerificationController {

    private final VerificationCodeDataService verificationCodeDataService;

    @Operation(
            summary = "Resend verification code",
            description = "Resends the OTP verification code to the user's registered email address. "
                    + "Typically used if the original code expired or was not received.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email of the user requesting a new verification code",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResendVerificationCodeDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Resend Code Request",
                                            value = """
                        {
                          "email": "john.doe@example.com"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Verification code resent successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data or user not found"
                    )
            }
    )
    @PostMapping("/resend")
    public void resendVerificationCode(
            @Valid @RequestBody final @NonNull ResendVerificationCodeDto resendVerificationCodeDto
    ) {
        this.verificationCodeDataService.resendVerificationCode(resendVerificationCodeDto);
    }

    @Operation(
            summary = "Verify email with OTP",
            description = "Verifies the user's email address by validating the provided OTP code. "
                    + "Once verified, the user's account is enabled for login.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email and OTP code for verification",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = VerificationCodeDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Verify Email Request",
                                            value = """
                        {
                          "email": "john.doe@example.com",
                          "code": "123456"
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email verified successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid code or email"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    @PostMapping("/verify")
    public void verifyCode(
            @Valid @RequestBody final @NonNull VerificationCodeDto verificationCodeDto
    ) {
        this.verificationCodeDataService.verifyCode(verificationCodeDto);
    }
}

