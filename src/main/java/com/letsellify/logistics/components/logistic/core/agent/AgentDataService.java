package com.letsellify.logistics.components.logistic.core.agent;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistic.core.agent.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistic.core.agent.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistic.core.agent.exception.UnapprovedAgentException;
import com.letsellify.logistics.components.logistic.core.agent.rest.dto.AgentInfoDto;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.AgentPersonalInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentResource;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;
import com.letsellify.logistics.components.logistic.core.kyc.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistic.core.request.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
            return this.agentManager.uploadKycDocument(authentication.getName(),kycDocumentType,file).getKycIdentification();
        }
        catch (final NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }

    public void deleteKyc(final @NonNull Authentication authentication, final @NonNull String kycIdentification) {
        try {
            this.agentManager.deleteKyc(authentication.getName(),kycIdentification);
        }
        catch (final NoSuchAgentException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public AgentPersonalInfoResource setInfo(final @NonNull Authentication authentication, final @NonNull AgentInfoDto personalInfoDto) {
        try {
            return this.agentManager.setInfo(authentication.getName(), personalInfoDto.personalInfo(), personalInfoDto.contactInfo(), personalInfoDto.storeDetail())
                                         .getResource();
        }
        catch (final NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final NoSuchStateException | IllegalLGAException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public LogisticAgentInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
        try {
            return this.agentManager.confirmInfoSubmissionForApproval(authentication.getName())
                                         .getResource();
        }
        catch (final NoSuchAgentException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


    public LogisticAgentInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
        try {
            return this.agentManager.viewPersonalInfoAfterApproval(authentication.getName())
                                         .getResource();
        }
        catch (final NoKycRecordFoundException | NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }


    public LogisticAgentResource setCurrentlyAcceptingStorage(final @NonNull Authentication authentication, final boolean status) {
        try {
            return this.agentManager.setCurrentlyAcceptingStorage(authentication.getName(), status)
                                         .getResource();
        }
        catch (final UnapprovedAgentException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

}
