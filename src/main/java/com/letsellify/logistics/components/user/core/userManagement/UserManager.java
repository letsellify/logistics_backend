package com.letsellify.logistics.components.user.core.userManagement;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUsers;
import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.userManagement.database.repository.UserRepository;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserExistsException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserUnAuthorizedException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.VerificationCodeManager;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final VerificationCodeManager verificationCodeManager;

    public LogisticsAppUser createUser(@NonNull final String name, @NonNull final String email, @NonNull final String password, final @NonNull LogisticsAppRole userRole, final boolean enabled) throws UserExistsException, UserUnAuthorizedException {
        if (this.repository.existsByEmail(email)) {
            throw new UserExistsException("User with email " + email + " already exists");
        }
        if (userRole.equals(LogisticsAppRole.ADMIN)) {
            throw new UserUnAuthorizedException("ADMIN user cannot be created");
        }
        final UserEntity entity = UserEntity.getInstance(name, email, this.passwordEncoder.encode(password), userRole, enabled);
        this.repository.save(entity);
        // send verificationCode
        if (!enabled) {
            this.verificationCodeManager.generateAndSendVerificationCode(email, userRole);
        }

        return new LogisticsAppUser(entity);
    }



    public LogisticsAppUser getUserById(@NonNull final UUID id) throws UserNotFoundException {
        final UserEntity entity = this.repository.findById(id).orElseThrow(
          () -> new UserNotFoundException("User with id " + id + " not found."));
        return new LogisticsAppUser(entity);

    }

    // try to return paginated results here instead
    public LogisticsAppUsers getAllUsers(final @NonNull Pageable pageable) {
        final Page<UserEntity> entityListPage = this.repository.findAll(pageable);
        return new LogisticsAppUsers(entityListPage);

    }


    public LogisticsAppUser getUserByEmail(@NonNull final String email) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email).orElseThrow(
          () -> new UserNotFoundException("User with email " + email + " not found."));
        return new LogisticsAppUser(entity);
    }

    // remove
    @Transactional
    public LogisticsAppUser updateUser(final String name, @NonNull final String email) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email).orElseThrow(
                                                    () -> new UserNotFoundException("User with email " + email + " not found"));
        entity.setName(name);
        entity.setEmail(email);
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public LogisticsAppUser updateUserPassword(@NonNull final UUID id, @NonNull final String password) throws UserNotFoundException {
        final UserEntity entity = this.repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        entity.setPassword(this.passwordEncoder.encode(password));
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public LogisticsAppUser updateUserPassword(final @NonNull String email, final @NonNull String password) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        entity.setPassword(this.passwordEncoder.encode(password));
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public void deleteUser(final @NonNull UUID id) {
        this.repository.deleteById(id);
    }


    public void deleteUser(final @NonNull String email) {
        this.repository.deleteByEmail(email);
    }

//    public LogisticsKyc setKycDocumentType(final @NonNull String username, final @NonNull KycDocumentTypeDto kycDocumentTypeDto) throws UserNotFoundException {
//        final UserEntity entity = this.repository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
//        return this.kycManager.setKycDocumentType(entity.getId(),kycDocumentTypeDto.getKycDocument());
//    }

//    public LogisticsKyc uploadKycDocument(final @NonNull String username, final @NonNull MultipartFile multipartFile) throws UserNotFoundException, LogisticsS3IOException, KycResourceNotFoundException, KycBadRequestException {
//        final UserEntity entity = this.repository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
//        return this.kycManager.uploadKycDocument(entity.getId(),multipartFile);
//    }

}
