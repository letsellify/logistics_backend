package com.letsellify.logistics.components.logistics.core.dispatcherManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.*;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.DispatcherProfilePhotoExistsException;
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
 * Time:13:03
 */

@Service
@RequiredArgsConstructor
public class DispatcherDataService {
    private final DispatcherManager dispatcherManager;

    public String uploadProfilePhoto(final @NonNull Authentication authentication, final @NonNull MultipartFile file) {
        try {
            return this.dispatcherManager.uploadProfilePhoto(authentication.getName(), file);
        } catch (DispatcherProfilePhotoExistsException e) {
            throw new LogisticsRestException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        } catch (DispatcherProfileCompleteException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    public DispatcherProfileInfoResource setProfile(final @NonNull Authentication authentication, final @NonNull DispatcherProfileDto profileDto) {
        try {
            return this.dispatcherManager.setProfile(authentication.getName(), profileDto.personalInfo(), profileDto.contactInfo(), profileDto.dispatchDetail(), profileDto.guarantorInfo(), profileDto.kyc())
                    .getResource();
        } catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final NoSuchStateException | IllegalLGAException | DispatcherProfileCompleteException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


    public DispatcherProfileInfoResource getProfile(final @NonNull Authentication authentication) {
        try {
            return this.dispatcherManager.getProfile(authentication.getName())
                    .getResource();
        } catch (InCompleteDispatcherProfileException | NoSuchDispatcherException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


}
