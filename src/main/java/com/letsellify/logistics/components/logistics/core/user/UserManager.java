package com.letsellify.logistics.components.logistics.core.user;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;
import com.letsellify.logistics.components.logistics.core.kyc.KycManager;
import com.letsellify.logistics.components.logistics.core.kyc.data.LogisticsKyc;
import com.letsellify.logistics.components.logistics.core.kyc.exception.KycBadRequestException;
import com.letsellify.logistics.components.logistics.core.kyc.exception.KycResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.kyc.rest.dto.KycDocumentTypeDto;
import com.letsellify.logistics.components.logistics.core.user.data.LogisticsAppUser;
import com.letsellify.logistics.components.logistics.core.user.data.LogisticsAppUsers;
import com.letsellify.logistics.components.logistics.core.user.database.entity.UserEntity;
import com.letsellify.logistics.components.logistics.core.user.database.repository.UserRepository;
import com.letsellify.logistics.components.logistics.core.user.exception.UserExistsException;
import com.letsellify.logistics.components.logistics.core.user.exception.UserNotFoundException;

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
    private final KycManager kycManager;

    public LogisticsAppUser createUser(@NonNull final String name, @NonNull final String email, @NonNull final String password) throws UserExistsException {
        if (this.repository.existsByEmail(email)) {
            throw new UserExistsException("User with email " + email + " already exists");
        }
        final UserEntity entity = UserEntity.getInstance(name, email, this.passwordEncoder.encode(password));
        this.repository.save(entity);
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

    public LogisticsKyc setKycDocumentType(final @NonNull String username, final @NonNull KycDocumentTypeDto kycDocumentTypeDto) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
        return this.kycManager.setKycDocumentType(entity.getId(),kycDocumentTypeDto.getKycDocument());
    }

    public LogisticsKyc uploadKycDocument(final @NonNull String username, final @NonNull MultipartFile multipartFile) throws UserNotFoundException, LogisticsS3IOException, KycResourceNotFoundException, KycBadRequestException {
        final UserEntity entity = this.repository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));
        return this.kycManager.uploadKycDocument(entity.getId(),multipartFile);
    }

}
