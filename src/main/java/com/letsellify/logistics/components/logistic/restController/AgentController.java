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

import com.letsellify.logistics.components.logistic.core.agent.AgentDataService;
import com.letsellify.logistics.components.logistic.core.agent.rest.dto.AgentInfoDto;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.AgentPersonalInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentResource;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:14:02
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent/")
@Tag(name = "Agents API", description = "API's for agents")
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
    public AgentPersonalInfoResource setPersonalInfo(final @NonNull Authentication authentication, final @Valid @RequestBody AgentInfoDto personalInfoDto) {
        return this.agentDataService.setInfo(authentication, personalInfoDto);
    }

    @PostMapping("confirm-info")
    public LogisticAgentInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
        return this.agentDataService.confirmInfoSubmissionForApproval(authentication);
    }

    @GetMapping("view-info")
    public LogisticAgentInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
        return this.agentDataService.viewPersonalInfoAfterApproval(authentication);
    }

    @PatchMapping("storage-status")
    public LogisticAgentResource setCurrentlyAcceptingStorage(final @NonNull Authentication authentication, @RequestParam(name = "accepting") final boolean acceptingStorage) {
        return this.agentDataService.setCurrentlyAcceptingStorage(authentication,acceptingStorage);
    }
}
