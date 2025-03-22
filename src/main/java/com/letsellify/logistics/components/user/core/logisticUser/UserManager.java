package com.letsellify.logistics.components.user.core.logisticUser;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.user.core.logisticUser.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.logisticUser.data.LogisticsAppUsers;
import com.letsellify.logistics.components.user.core.logisticUser.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.logisticUser.database.repository.UserRepository;
import com.letsellify.logistics.components.user.core.logisticUser.event.UnverifiedUserCreatedEvent;
import com.letsellify.logistics.components.user.core.logisticUser.event.UserOfRoleAgentCreated;
import com.letsellify.logistics.components.user.core.logisticUser.event.UserOfRoleDispatcherCreated;
import com.letsellify.logistics.components.user.core.logisticUser.event.UserOfRoleVendorCreated;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserExistsException;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.logisticUser.exception.UserUnAuthorizedException;
import com.letsellify.logistics.components.user.core.socialSignupAndLogin.data.LogisticOAuth2User;
import com.letsellify.logistics.components.user.core.verificationCode.event.UserVerifiedEvent;
import com.letsellify.logistics.components.user.core.verificationCode.exception.UnableToCreateVerificationCodeException;

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
    private final static String DEFAULT_PROVIDER = "Letsellify_Logistics";

    @Async
    @EventListener
    @Transactional
    public void on(final UserVerifiedEvent userVerifiedEvent) {
        final UserEntity entity;
        final LogisticAppRole userRole;
        try {
            entity = this.repository.findByEmail(userVerifiedEvent.getUserEmail())
                                    .orElseThrow(() -> new UserNotFoundException("User "+ userVerifiedEvent.getUserEmail() + " does not exist"));
            userRole = entity.getRole();
        }
        catch (final UserNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        entity.activate();
        final LogisticsAppUser user = new LogisticsAppUser(entity);
        switch (userRole) {
            case VENDOR -> this.eventPublisher.publishEvent(new UserOfRoleVendorCreated(user));
            case DISPATCHER -> this.eventPublisher.publishEvent(new UserOfRoleDispatcherCreated(user));
            case AGENT -> this.eventPublisher.publishEvent(new UserOfRoleAgentCreated(user));
            default -> log.warn("No event published for role: {}", userRole);
        }
        this.repository.save(entity);
    }

    @Transactional
    LogisticsAppUser createUser(final @NonNull String name, final @NonNull  String email, final @NonNull  String password, final LogisticAppRole userRole, final boolean enabled) throws UserExistsException, UserUnAuthorizedException, UserNotFoundException, UnableToCreateVerificationCodeException {
        if (this.repository.existsByEmail(email)) {
            throw new UserExistsException("User with email " + email + " already exists");
        }
        if (userRole.equals(LogisticAppRole.ADMIN)) {
            throw new UserUnAuthorizedException("ADMIN user cannot be created");
        }
        final UserEntity entity = UserEntity.create(name, email, this.passwordEncoder.encode(password), userRole, enabled, DEFAULT_PROVIDER);
        // send verificationCode
        final LogisticsAppUser appUser = new LogisticsAppUser(entity);
        if (!enabled) {
            this.eventPublisher.publishEvent(new UnverifiedUserCreatedEvent(appUser));
        }
        this.repository.save(entity);
        return appUser;
    }


    @Transactional
    public LogisticsAppUser processOAuth2User(final @NonNull LogisticOAuth2User oAuth2User) throws UserExistsException {
        final UserEntity entity;
        try {
            entity = UserEntity.create(oAuth2User.getName(), oAuth2User.getEmail(), null, null,false, oAuth2User.getProvider());
            this.repository.save(entity);
            return new LogisticsAppUser(entity);
        }
        catch (final DataIntegrityViolationException e) {
            throw new UserExistsException("User with email " + oAuth2User.getEmail() + " already exists");
        }
    }

    @Transactional
    public void assignRoleToUser(final @NonNull String username, final LogisticAppRole logisticsAppRole) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(username)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found."));
        entity.setRole(logisticsAppRole);
        entity.activate();
        final LogisticAppRole userRole = entity.getRole();
        final LogisticsAppUser appUser = new LogisticsAppUser(entity);
        switch (userRole) {
            case VENDOR -> this.eventPublisher.publishEvent(new UserOfRoleVendorCreated(appUser));
            case DISPATCHER -> this.eventPublisher.publishEvent(new UserOfRoleDispatcherCreated(appUser));
            case AGENT -> this.eventPublisher.publishEvent(new UserOfRoleAgentCreated(appUser));
            default -> log.warn("No event published for role: {}", userRole);
        }
        this.repository.save(entity);
    }

    public LogisticsAppUser getUserById(@NonNull final UUID id) throws UserNotFoundException {
        final UserEntity entity = this.repository.findById(id)
                                                 .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
        return new LogisticsAppUser(entity);

    }

    // try to return paginated results here instead
    public LogisticsAppUsers getAllUsers(final @NonNull Pageable pageable) {
        final Page<UserEntity> entityListPage = this.repository.findAll(pageable);
        return new LogisticsAppUsers(entityListPage);
    }


    public LogisticsAppUser getUserByEmail(@NonNull final String email) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
        return new LogisticsAppUser(entity);
    }

    // remove
    @Transactional
    public LogisticsAppUser updateUser(final String name, @NonNull final String email) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        entity.updateName(name);
        entity.updateEmail(email);
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public LogisticsAppUser updateUserPassword(@NonNull final UUID id, @NonNull final String password) throws UserNotFoundException {
        final UserEntity entity = this.repository.findById(id)
                                                 .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        entity.updatePassword(this.passwordEncoder.encode(password));
        this.repository.save(entity);
        return new LogisticsAppUser(entity);
    }


    public LogisticsAppUser updateUserPassword(final @NonNull String email, final @NonNull String password) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmail(email)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
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


}
