package com.letsellify.logistics.components.user.core.userManagement;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsConflictException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserExistsException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserUnAuthorizedException;
import com.letsellify.logistics.components.user.core.userManagement.rest.dto.CreateUserDto;
import com.letsellify.logistics.components.user.core.userManagement.rest.dto.UpdateUserDto;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResources;

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
            return this.userManager.createUser(createUserDto.getName(), createUserDto.getEmail(), createUserDto.getPassword(), createUserDto.getRole(), false)
                                   .getResource();
        }
        catch (final UserExistsException e) {
            throw new LogisticsConflictException("409", e);
        }
        catch (final UserUnAuthorizedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
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

//    public KycResource setKycDocumentType(final @NonNull Authentication authentication, final @NonNull KycDocumentTypeDto kycDocumentTypeDto) {
//        try {
//            return this.userManager.setKycDocumentType(authentication.getName(),kycDocumentTypeDto)
//                     .toKycResource();
//        }
//        catch (final UserNotFoundException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//    }

//    public KycDocumentUploadResource uploadKycDocument(final Authentication authentication, final MultipartFile multipartFile) {
//        try {
//            return this.userManager.uploadKycDocument(authentication.getName(), multipartFile)
//                                   .toKycDocumentUploadResource();
//        }
//        catch (final UserNotFoundException | KycResourceNotFoundException | KycBadRequestException e) {
//            throw new LogisticsBadRequestException(e.getMessage());
//        }
//        catch (final LogisticsS3IOException e) {
//            throw new LogisticsInternalServerErrorException(e.getMessage());
//        }
//    }

}
