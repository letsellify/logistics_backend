package com.letsellify.logistics.components.user.restController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.user.core.socialSignupAndLogin.SocialLoginAndSignUpDataService;
import com.letsellify.logistics.components.user.core.socialSignupAndLogin.rest.dto.RoleSelectionDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @PostMapping("/role-selection")
    public ResponseEntity<?> assignRole(final HttpServletResponse httpServletResponse, @RequestBody final @Valid RoleSelectionDto roleSelectionDto) {
        this.socialLoginAndSignUpDataService.assignRole(httpServletResponse, roleSelectionDto.token(), roleSelectionDto.role());
        return ResponseEntity.ok(Map.of("message", "Role Assigned"));
    }
}
