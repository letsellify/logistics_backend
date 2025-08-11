package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherInfoDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherPersonalInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:03:45
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dispatcher/")
@Tag(name = "Dispatchers API", description = "API's for dispatchers")
public class DispatcherController {
    private final DispatcherDataService dispatcherDataService;

    @PostMapping("kyc-upload")
    public String uploadKycDocument(final @NonNull Authentication authentication, @RequestParam final @NonNull KycDocumentType documentType, @RequestParam final MultipartFile file, HttpServletRequest httpServletRequest) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty.");
        }
        final String contentType = file.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "application/pdf").contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG, PNG, and PDF files are allowed.");
        }
        System.out.println("File received: " + file.getOriginalFilename() + " with content type: " + contentType);
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) { // replace with your actual cookie name
                    String token = cookie.getValue();
                    System.out.println("Access Token from Cookie: " + token);
                }
            }
        } else {
            System.out.println("No cookies found in request.");
        }

        return this.dispatcherDataService.uploadKyc(authentication, documentType, file);
    }

    @DeleteMapping("kyc-upload")
    public void deleteKyc(final @NonNull Authentication authentication, @RequestParam final @NonNull String kycIdentification) {
        this.dispatcherDataService.deleteKyc(authentication, kycIdentification);
    }

    @PostMapping("personal-info")
    public DispatcherPersonalInfoResource setPersonalInfo(final @NonNull Authentication authentication, final @Valid @RequestBody DispatcherInfoDto infoDto) {
        return this.dispatcherDataService.setPersonalInfo(authentication, infoDto);
    }

    @PostMapping("confirm-info")
    public LogisticDispatcherInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
        return this.dispatcherDataService.confirmInfoSubmissionForApproval(authentication);
    }

    @GetMapping("view-info")
    public LogisticDispatcherInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
        return this.dispatcherDataService.viewPersonalInfoAfterApproval(authentication);
    }

    @PatchMapping("delivery-status")
    public DispatcherResource setCurrentlyAcceptingDelivery(final @NonNull Authentication authentication, @RequestParam(name = "accepting") final boolean acceptingDelivery) {
        return this.dispatcherDataService.setCurrentlyAcceptingDelivery(authentication, acceptingDelivery);
    }

}
