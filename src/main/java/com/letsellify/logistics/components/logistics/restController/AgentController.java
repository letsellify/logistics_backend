package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.agentManagement.AgentDataService;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentInfoDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Date:2/26/25
 * Time:14:02
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent/")
@Tag(name = "Agents API", description = "API's for agents")
@Hidden
public class AgentController {
    private final AgentDataService agentDataService;

    @PostMapping("kyc-upload")
    public String uploadKycDocument(final @NonNull Authentication authentication, @RequestParam final @NonNull KycDocumentType documentType, @RequestParam final @NonNull MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty.");
        }
        final String contentType = file.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "application/pdf").contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG, PNG, and PDF files are allowed.");
        }
        return this.agentDataService.uploadKyc(authentication, documentType, file);
    }

    @DeleteMapping("kyc-upload")
    public void deleteKyc(final @NonNull Authentication authentication, @RequestParam final @NonNull String kycIdentification) {
        this.agentDataService.deleteKyc(authentication, kycIdentification);
    }

    @PostMapping("personal-info")
    public AgentProfileInfoResource setPersonalInfo(final @NonNull Authentication authentication, final @Valid @RequestBody AgentInfoDto personalInfoDto) {
        return this.agentDataService.setInfo(authentication, personalInfoDto);
    }

    @PostMapping("confirm-info")
    public AgentInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
        return this.agentDataService.confirmInfoSubmissionForApproval(authentication);
    }

    @GetMapping("view-info")
    public AgentInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
        return this.agentDataService.viewPersonalInfoAfterApproval(authentication);
    }

    @PatchMapping("storage-status")
    public AgentResource setCurrentlyAcceptingStorage(final @NonNull Authentication authentication, @RequestParam(name = "accepting") final boolean acceptingStorage) {
        return this.agentDataService.setCurrentlyAcceptingStorage(authentication, acceptingStorage);
    }
}
