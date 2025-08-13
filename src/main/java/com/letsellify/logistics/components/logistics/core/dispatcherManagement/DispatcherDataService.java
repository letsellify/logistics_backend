package com.letsellify.logistics.components.logistics.core.dispatcherManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.*;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import com.letsellify.logistics.components.logistics.core.kycManagement.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:03
 */

@Service
@RequiredArgsConstructor
public class DispatcherDataService {
    private final DispatcherManager dispatcherManager;

    public String uploadKyc(final @NonNull Authentication authentication, final @NonNull KycDocumentType kycDocumentType, final @NonNull MultipartFile file) {
        try {
            return this.dispatcherManager.uploadKycDocument(authentication.getName(), kycDocumentType, file).getKycIdentification();
        } catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final DispatcherApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (final IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }

    public void deleteKyc(final @NonNull Authentication authentication, final @NonNull String kycIdentification) {
        try {
            this.dispatcherManager.deleteKyc(authentication.getName(), kycIdentification);
        } catch (final NoSuchDispatcherException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final DispatcherApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public DispatcherProfileInfoResource setProfile(final @NonNull Authentication authentication, final @NonNull DispatcherProfileDto profileDto) {
        try {
            return this.dispatcherManager.setInfo(authentication.getName(), profileDto.personalInfo(), profileDto.contactInfo(), profileDto.dispatchDetail(), profileDto.guarantorInfo(), profileDto.kyc())
                    .getResource();
        } catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final NoSuchStateException | IllegalLGAException | DispatcherProfileCompleteException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public DispatcherProfileInfoResources getAllAwaitingApprovals(final @NonNull Pageable pageable) {
       return this.dispatcherManager.getAllAwaitingApproval(pageable)
               .getResource();
    }

    public DispatcherProfileInfoResource getProfile(final @NonNull Authentication authentication) {
        try {
            return this.dispatcherManager.getProfile(authentication.getName())
                    .getResource();
        } catch (InCompleteDispatcherProfileException | NoSuchDispatcherException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

//    public LogisticDispatcherInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
//        try {
//            return this.dispatcherManager.confirmInfoSubmissionForApproval(authentication.getName())
//                    .getResource();
//        } catch (final NoSuchDispatcherException | NoKycRecordFoundException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        } catch (final DispatcherApprovedException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//    }


//    public LogisticDispatcherInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
//        try {
//            return this.dispatcherManager.viewPersonalInfoAfterApproval(authentication.getName())
//                    .getResource();
//        } catch (final NoKycRecordFoundException | NoSuchDispatcherException e) {
//            throw new LogisticsResourceNotFoundException(e.getMessage());
//        } catch (final UnapprovedDispatcherException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//    }


    public DispatcherResource setCurrentlyAcceptingDelivery(final @NonNull Authentication authentication, final boolean acceptingDelivery) {
        try {
            return this.dispatcherManager.setCurrentlyAcceptingDelivery(authentication.getName(), acceptingDelivery)
                    .getResource();
        } catch (final UnapprovedDispatcherException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }
}
