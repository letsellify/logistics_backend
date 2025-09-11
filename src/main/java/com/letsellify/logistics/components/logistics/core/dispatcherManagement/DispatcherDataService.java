package com.letsellify.logistics.components.logistics.core.dispatcherManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.*;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherNotificationDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.LgaPreferenceDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResources;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.LgaResource;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.DispatcherProfilePhotoExistsException;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
        } catch (DispatcherProfilePhotoExistsException | DispatcherProfileCompleteException e) {
            throw new LogisticsRestException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        } catch (NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    public DispatcherProfileInfoResource setProfile(final @NonNull Authentication authentication, final @NonNull DispatcherProfileDto profileDto) {
        try {
            return this.dispatcherManager.setProfile(authentication.getName(), profileDto.personalInformation(), profileDto.contactInformation(), profileDto.businessInformation(), profileDto.guarantorInformation(), profileDto.kyc())
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
        } catch (NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }


    public DispatcherLgaPreferenceResource addLgaPreference(final @NonNull Authentication authentication, final @NonNull LgaPreferenceDto lgaPreferenceDto) {
        try {
            return this.dispatcherManager.addLgaPreference(authentication.getName(), lgaPreferenceDto.pickUpLga(), lgaPreferenceDto.dropOffLga())
                    .getResource();
        } catch (NoSuchStateException e) {
            throw new LogisticsRestException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (InCompleteDispatcherProfileException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IllegalLGAException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (UnableToAddLgaPreferenceException e) {
            throw new LogisticsRestException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    public DispatcherLgaPreferenceResources getLgaPreferences(final @NonNull Authentication authentication) {
        try {
            return this.dispatcherManager.getLgaPreferences(authentication.getName())
                    .getResources();
        } catch (InCompleteDispatcherProfileException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }


    public void updateNotificationPreference(final @NonNull Authentication authentication, final @NonNull DispatcherNotificationDto notificationDto) {
        try {
            this.dispatcherManager.updateNotificationPreference(authentication.getName(), notificationDto.all());
        } catch (DispatcherReceiveAllNotificationException e) {
            throw new LogisticsRestException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (InCompleteDispatcherProfileException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }
    }

    public void deleteNotificationPreference(final @NonNull Authentication authentication, final @NonNull UUID preferenceId) {
        try {
            this.dispatcherManager.deletePreference(authentication.getName(),preferenceId);
        } catch (DispatcherReceiveAllNotificationException e) {
            throw new LogisticsRestException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchDispatcherPreferenceException | NoSuchDispatcherException e) {
            throw new LogisticsRestException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (InCompleteDispatcherProfileException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

}
