package com.letsellify.logistics.components.logistics.core.agentManagement;

import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentProfileCompleteException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentProfilePhotoExistsException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto.AgentProfileDto;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

//    public String uploadKyc(final @NonNull Authentication authentication, final @NonNull KycDocumentType kycDocumentType, final @NonNull MultipartFile file) {
//        try {
//            return this.agentManager.uploadKycDocument(authentication.getName(), kycDocumentType, file).getKycIdentification();
//        } catch (final NoSuchAgentException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        } catch (final AgentApprovedException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        } catch (final IOException e) {
//            throw new LogisticsInternalServerErrorException(e.getMessage());
//        }
//    }
//
//    public void deleteKyc(final @NonNull Authentication authentication, final @NonNull String kycIdentification) {
//        try {
//            this.agentManager.deleteKyc(authentication.getName(), kycIdentification);
//        } catch (final NoSuchAgentException | NoKycRecordFoundException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        } catch (final AgentApprovedException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//    }
//
//    public AgentProfileInfoResource setInfo(final @NonNull Authentication authentication, final @NonNull AgentInfoDto personalInfoDto) {
//        try {
//            return this.agentManager.setInfo(authentication.getName(), personalInfoDto.personalInfo(), personalInfoDto.contactInfo(), personalInfoDto.storeDetail())
//                    .getResource();
//        } catch (final NoSuchAgentException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        } catch (final NoSuchStateException | IllegalLGAException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//    }
//
//    public AgentInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
//        try {
//            return this.agentManager.confirmInfoSubmissionForApproval(authentication.getName())
//                    .getResource();
//        } catch (final NoSuchAgentException | NoKycRecordFoundException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        } catch (final AgentApprovedException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//    }
//
//
//    public AgentInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
//        try {
//            return this.agentManager.viewPersonalInfoAfterApproval(authentication.getName())
//                    .getResource();
//        } catch (final NoKycRecordFoundException | NoSuchAgentException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        }
//    }
//
//
//    public AgentResource setCurrentlyAcceptingStorage(final @NonNull Authentication authentication, final boolean status) {
//        try {
//            return this.agentManager.setCurrentlyAcceptingStorage(authentication.getName(), status)
//                    .getResource();
//        } catch (final UnapprovedAgentException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        } catch (final NoSuchAgentException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        }
//    }

    public String uploadProfilePhoto(final @NonNull Authentication authentication, final @NonNull MultipartFile file) {
        try {
            return this.agentManager.uploadProfilePhoto(authentication.getName(),file);
        } catch (AgentProfilePhotoExistsException | AgentProfileCompleteException e) {
            throw new LogisticsRestException(HttpStatus.CONFLICT, e.getMessage());
        } catch (NoSuchAgentException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }

    public AgentProfileInfoResource setProfile(final @NonNull Authentication authentication, final @NonNull AgentProfileDto profileDto) {
        try {
            return this.agentManager.setProfile(
                    authentication.getName(),
                    profileDto.personalInformation().name(),
                    profileDto.personalInformation().state(),
                    profileDto.personalInformation().lga(),
                    profileDto.personalInformation().address(),
                    profileDto.contactInformation().phone(),
                    profileDto.contactInformation().whatsAppPhone(),
                    profileDto.businessInformation().name(),
                    profileDto.businessInformation().state(),
                    profileDto.businessInformation().lga(),
                    profileDto.businessInformation().address(),
                    profileDto.guarantorInformation().fullName(),
                    profileDto.guarantorInformation().email(),
                    profileDto.guarantorInformation().state(),
                    profileDto.guarantorInformation().lga(),
                    profileDto.guarantorInformation().address(),
                    profileDto.guarantorInformation().phoneNumber(),
                    profileDto.guarantorInformation().whatsAppNumber(),
                    profileDto.guarantorInformation().career(),
                    profileDto.guarantorInformation().relationship(),
                    profileDto.kyc().type(),
                    profileDto.kyc().number()
            )
                    .getResource();
        } catch (NoSuchAgentException | AgentProfileCompleteException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NoSuchStateException | IllegalLGAException e) {
            throw new LogisticsRestException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public AgentProfileInfoResource getProfile(final @NonNull Authentication authentication) {
        try {
            return this.agentManager.getProfile(authentication.getName())
                    .getResource();
        } catch (NoSuchAgentException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
