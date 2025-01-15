package com.letsellify.logistics.components.logistics.core.user;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsConflictException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;
import com.letsellify.logistics.components.logistics.core.kyc.exception.KycBadRequestException;
import com.letsellify.logistics.components.logistics.core.kyc.exception.KycResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.kyc.rest.dto.KycDocumentTypeDto;
import com.letsellify.logistics.components.logistics.core.kyc.rest.resource.KycDocumentUploadResource;
import com.letsellify.logistics.components.logistics.core.kyc.rest.resource.KycResource;
import com.letsellify.logistics.components.logistics.core.user.exception.UserExistsException;
import com.letsellify.logistics.components.logistics.core.user.exception.UserNotFoundException;
import com.letsellify.logistics.components.logistics.core.user.rest.dto.CreateUserDto;
import com.letsellify.logistics.components.logistics.core.user.rest.dto.UpdateUserDto;
import com.letsellify.logistics.components.logistics.core.user.rest.resource.UserResource;
import com.letsellify.logistics.components.logistics.core.user.rest.resource.UserResources;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Service
@RequiredArgsConstructor
public class UserDataService {
    private final UserManager userManager;

    public UserResource createUser(@NonNull final CreateUserDto createUserDto) {
        try {
            return this.userManager.createUser(createUserDto.getName(), createUserDto.getEmail(), createUserDto.getPassword())
                                   .getResource();
        }
        catch (final UserExistsException e) {
            throw new LogisticsConflictException("409", e);
        }
    }

    public UserResource getUser(@NonNull final String email) {
        try {
            return this.userManager.getUserByEmail(email)
                                   .getResource();
        }
        catch (final UserNotFoundException e) {
            throw new LogisticsResourceNotFoundException(e);
        }

    }

    public UserResource updateUser(@NonNull final UpdateUserDto updateUserDto) {
        try {
            return this.userManager.updateUser(updateUserDto.getName(), updateUserDto.getEmail())
                                   .getResource();
        }
        catch (final UserNotFoundException e) {
            throw new LogisticsResourceNotFoundException(e);
        }
    }

    public void deleteUser(@NonNull final String email) {
        this.userManager.deleteUser(email);
    }

    public UserResources getAllUsers(@NonNull final Pageable pageable) {
        return this.userManager.getAllUsers(pageable)
                               .getResource();
    }

    public KycResource setKycDocumentType(final @NonNull Authentication authentication, final @NonNull KycDocumentTypeDto kycDocumentTypeDto) {
        try {
            return this.userManager.setKycDocumentType(authentication.getName(),kycDocumentTypeDto)
                     .toKycResource();
        }
        catch (final UserNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }

    public KycDocumentUploadResource uploadKycDocument(final Authentication authentication, final MultipartFile multipartFile) {
        try {
            return this.userManager.uploadKycDocument(authentication.getName(), multipartFile)
                                   .toKycDocumentUploadResource();
        }
        catch (final UserNotFoundException | KycResourceNotFoundException | KycBadRequestException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final LogisticsS3IOException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
        }
    }

}
