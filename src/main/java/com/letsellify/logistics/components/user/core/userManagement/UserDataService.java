package com.letsellify.logistics.components.user.core.userManagement;

import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsConflictException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserExistsException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserUnAuthorizedException;
import com.letsellify.logistics.components.user.core.userManagement.rest.dto.CreateUserDto;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.UnableToCreateVerificationCodeException;

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
            return this.userManager.createUser(createUserDto.getEmail(), createUserDto.getPassword(), createUserDto.getRole())
                                   .getResource();
        }
        catch (final UserExistsException e) {
            throw new LogisticsConflictException("409",e);
        }
        catch (final UserUnAuthorizedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final UserNotFoundException | UnableToCreateVerificationCodeException e) {
            throw new LogisticsInternalServerErrorException(e.getMessage());
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



//    // mark for removal
//    public UserResource updateUser(@NonNull final UpdateUserDto updateUserDto) {
//        try {
//            return this.userManager.updateUser(updateUserDto.getName(), updateUserDto.getEmail())
//                                   .getResource();
//        }
//        catch (final UserNotFoundException e) {
//            throw new LogisticsResourceNotFoundException(e);
//        }
//    }
//
//    //mark for removal
//    public void deleteUser(@NonNull final String email) {
//        this.userManager.deleteUser(email);
//    }
//
//    public UserResources getAllUsers(@NonNull final Pageable pageable) {
//        return this.userManager.getAllUsers(pageable)
//                               .getResource();
//    }
}
