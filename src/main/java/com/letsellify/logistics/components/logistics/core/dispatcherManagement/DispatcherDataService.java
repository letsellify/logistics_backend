package com.letsellify.logistics.components.logistics.core.dispatcherManagement;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;

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
        catch (final LogisticsS3IOException e) {
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
    }

    public void acceptLogisticsRequest(final @NonNull Authentication authentication, final @NonNull String logisticsId) {
    }
}
