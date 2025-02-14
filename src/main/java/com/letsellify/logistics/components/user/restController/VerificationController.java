package com.letsellify.logistics.components.user.restController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.user.core.verificationCodeManagement.VerificationCodeDataService;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto.ResendVerificationCodeDto;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto.VerificationCodeDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:17:01
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verification")
@Tag(name = "Verification API", description = "API's for user verification")
public class VerificationController {
    private final VerificationCodeDataService verificationCodeDataService;

    @PostMapping("/resend")
    public void resendVerificationCode(@Valid @RequestBody final ResendVerificationCodeDto resendVerificationCodeDto) {
        this.verificationCodeDataService.resendVerificationCode(resendVerificationCodeDto);
    }

    @PostMapping("/verify")
    public void resendVerificationCode(@Valid @RequestBody final VerificationCodeDto verificationCodeDto) {
        this.verificationCodeDataService.verifyCode(verificationCodeDto);
    }

}
