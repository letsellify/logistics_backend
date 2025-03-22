package com.letsellify.logistics.components.logistic.restController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.letsellify.logistics.components.logistic.core.dispatcher.DispatcherDataService;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto.DispatcherPersonalInfoDto;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.DispatcherPersonalInfoResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherResource;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    public String uploadKycDocument(final @NonNull Authentication authentication, @RequestParam final @NonNull KycDocumentType documentType, @RequestParam final  MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty.");
        }
        final String contentType = file.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "application/pdf").contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG, PNG, and PDF files are allowed.");
        }
        return this.dispatcherDataService.uploadKyc(authentication, documentType, file);
    }

    @DeleteMapping("kyc-upload")
    public void deleteKyc(final @NonNull Authentication authentication, @RequestParam final @NonNull String kycIdentification) {
        this.dispatcherDataService.deleteKyc(authentication, kycIdentification);
    }

    @PostMapping("personal-info")
    public DispatcherPersonalInfoResource setPersonalInfo(final @NonNull Authentication authentication, final @Valid @RequestBody DispatcherPersonalInfoDto personalInfoDto) {
        return this.dispatcherDataService.setPersonalInfo(authentication, personalInfoDto);
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
    public LogisticDispatcherResource setCurrentlyAcceptingDelivery(final @NonNull Authentication authentication, @RequestParam(name = "accepting") final boolean acceptingDelivery) {
        return this.dispatcherDataService.setCurrentlyAcceptingDelivery(authentication,acceptingDelivery);
    }

}
