package com.letsellify.logistics.components.logistics.core.agentManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.UnapprovedAgentException;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentInfoDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import com.letsellify.logistics.components.logistics.core.kycManagement.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:01
 */

@Service
@RequiredArgsConstructor
public class AgentDataService {
    private final AgentManager agentManager;

    public String uploadKyc(final @NonNull Authentication authentication, final @NonNull KycDocumentType kycDocumentType, final @NonNull MultipartFile file) {
        try {
            return this.agentManager.uploadKycDocument(authentication.getName(), kycDocumentType, file).getKycIdentification();
        } catch (final NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (final IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }

    public void deleteKyc(final @NonNull Authentication authentication, final @NonNull String kycIdentification) {
        try {
            this.agentManager.deleteKyc(authentication.getName(), kycIdentification);
        } catch (final NoSuchAgentException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public AgentProfileInfoResource setInfo(final @NonNull Authentication authentication, final @NonNull AgentInfoDto personalInfoDto) {
        try {
            return this.agentManager.setInfo(authentication.getName(), personalInfoDto.personalInfo(), personalInfoDto.contactInfo(), personalInfoDto.storeDetail())
                    .getResource();
        } catch (final NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final NoSuchStateException | IllegalLGAException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public AgentInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
        try {
            return this.agentManager.confirmInfoSubmissionForApproval(authentication.getName())
                    .getResource();
        } catch (final NoSuchAgentException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


    public AgentInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
        try {
            return this.agentManager.viewPersonalInfoAfterApproval(authentication.getName())
                    .getResource();
        } catch (final NoKycRecordFoundException | NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }


    public AgentResource setCurrentlyAcceptingStorage(final @NonNull Authentication authentication, final boolean status) {
        try {
            return this.agentManager.setCurrentlyAcceptingStorage(authentication.getName(), status)
                    .getResource();
        } catch (final UnapprovedAgentException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (final NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

}
