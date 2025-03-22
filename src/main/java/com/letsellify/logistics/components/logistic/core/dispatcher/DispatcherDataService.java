package com.letsellify.logistics.components.logistic.core.dispatcher;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.DispatcherApprovedException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.UnapprovedDispatcherException;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto.DispatcherPersonalInfoDto;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.DispatcherPersonalInfoResource;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;
import com.letsellify.logistics.components.logistic.core.kyc.exception.NoKycRecordFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
            return this.dispatcherManager.uploadKycDocument(authentication.getName(),kycDocumentType,file).getKycIdentification();
        }
        catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final DispatcherApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }

    public void deleteKyc(final @NonNull Authentication authentication, final @NonNull String kycIdentification) {
        try {
            this.dispatcherManager.deleteKyc(authentication.getName(),kycIdentification);
        }
        catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final DispatcherApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public DispatcherPersonalInfoResource setPersonalInfo(final @NonNull Authentication authentication, final @NonNull DispatcherPersonalInfoDto personalInfoDto) {
        try {
            return this.dispatcherManager.setPersonalInfo(authentication.getName(), personalInfoDto.whatsAppPhone(), personalInfoDto.phone(), personalInfoDto.state(), personalInfoDto.lga(), personalInfoDto.address())
                     .getResource();
        }
        catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

    public LogisticDispatcherInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
        try {
            return this.dispatcherManager.confirmInfoSubmissionForApproval(authentication.getName())
                                         .getResource();
        }
        catch (final NoSuchDispatcherException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final DispatcherApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


    public LogisticDispatcherInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
        try {
            return this.dispatcherManager.viewPersonalInfoAfterApproval(authentication.getName())
                                         .getResource();
        }
        catch (final NoKycRecordFoundException | NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
        catch (final UnapprovedDispatcherException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


    public LogisticDispatcherResource setCurrentlyAcceptingDelivery(final @NonNull Authentication authentication, final boolean acceptingDelivery) {
        try {
            return this.dispatcherManager.setCurrentlyAcceptingDelivery(authentication.getName(), acceptingDelivery)
                     .getResource();
        }
        catch (final UnapprovedDispatcherException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }
}
