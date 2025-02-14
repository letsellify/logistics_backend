package com.letsellify.logistics.components.user.core.userManagement;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUsers;
import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.userManagement.database.repository.UserRepository;
import com.letsellify.logistics.components.user.core.userManagement.event.UnverifiedUserCreatedEvent;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleAgentCreated;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleDispatcherCreated;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleVendorCreated;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserExistsException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserUnAuthorizedException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.event.UserVerifiedEvent;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.UnableToCreateVerificationCodeException;

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
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LogisticsAppUser createUser(@NonNull final String name, @NonNull final String email, @NonNull final String password, final @NonNull LogisticsAppRole userRole, final boolean enabled) throws UserExistsException, UserUnAuthorizedException, UserNotFoundException, UnableToCreateVerificationCodeException {
        if (this.repository.existsByEmail(email)) {
            throw new UserExistsException("User with email " + email + " already exists");
        }
        if (userRole.equals(LogisticsAppRole.ADMIN)) {
            throw new UserUnAuthorizedException("ADMIN user cannot be created");
        }
        final UserEntity entity = UserEntity.create(name, email, this.passwordEncoder.encode(password), userRole, enabled);

        // send verificationCode
        if (!enabled) {
            this.eventPublisher.publishEvent(new UnverifiedUserCreatedEvent(entity.getEmail(), entity.getRole()));
        }

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

    // remove
    @Transactional
    public LogisticsAppUser updateUser(final String name, @NonNull final String email) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email).orElseThrow(
                                                    () -> new UserNotFoundException("User with email " + email + " not found"));
        entity.updateName(name);
        entity.updateEmail(email);
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public LogisticsAppUser updateUserPassword(@NonNull final UUID id, @NonNull final String password) throws UserNotFoundException {
        final UserEntity entity = this.repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        entity.updatePassword(this.passwordEncoder.encode(password));
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public LogisticsAppUser updateUserPassword(final @NonNull String email, final @NonNull String password) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        entity.updatePassword(this.passwordEncoder.encode(password));
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public void deleteUser(final @NonNull UUID id) {
        this.repository.deleteById(id);
    }


    public void deleteUser(final @NonNull String email) {
        this.repository.deleteByEmail(email);
    }

    @Async
    @EventListener
    @Transactional
    public void on(final UserVerifiedEvent userVerifiedEvent) {
        final UserEntity entity;
        final LogisticsAppRole userRole;
        final String username;
        try {
            entity = this.repository.findByEmail(userVerifiedEvent.getUserEmail()).orElseThrow(() -> new UserNotFoundException("User "+ userVerifiedEvent.getUserEmail() + " does not exist"));
            userRole = entity.getRole();
            username = entity.getEmail();
        }
        catch (final UserNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        entity.activate();
        switch (userRole) {
            case VENDOR -> this.eventPublisher.publishEvent(new UserOfRoleVendorCreated(username));
            case DISPATCHER -> this.eventPublisher.publishEvent(new UserOfRoleDispatcherCreated(username));
            case AGENT -> this.eventPublisher.publishEvent(new UserOfRoleAgentCreated(username));
            default -> log.warn("No event published for role: {}", userRole);
        }
        this.repository.save(entity);
    }

}
