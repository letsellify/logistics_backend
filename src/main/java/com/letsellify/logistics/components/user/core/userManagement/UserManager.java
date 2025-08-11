package com.letsellify.logistics.components.user.core.userManagement;

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
import com.letsellify.logistics.components.logistics.core.agentManagement.event.AgentNameUpdateEvent;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.event.DispatcherNameUpdateEvent;
import com.letsellify.logistics.components.logistics.core.vendorManagement.event.VendorNameUpdateEvent;
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
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.data.LogisticOAuth2User;
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

    @Async
    @EventListener
    @Transactional
    public void on(final AgentNameUpdateEvent agentNameUpdateEvent) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmailAndRole(agentNameUpdateEvent.getAgentEmail(), LogisticAppRole.AGENT)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email "+ agentNameUpdateEvent.getAgentEmail() + " and role" + LogisticAppRole.AGENT + " not found."));
        entity.updateName(agentNameUpdateEvent.getAgentNameAfterUpdate());
        this.repository.save(entity);
    }

    @Async
    @EventListener
    @Transactional
    public void on(final VendorNameUpdateEvent vendorNameUpdateEvent) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmailAndRole(vendorNameUpdateEvent.getVendorEmail(), LogisticAppRole.VENDOR)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email "+ vendorNameUpdateEvent.getVendorEmail() + " and role" + LogisticAppRole.VENDOR + " not found."));
        entity.updateName(vendorNameUpdateEvent.getVendorNameAfterUpdate());
        this.repository.save(entity);
    }

    @Async
    @EventListener
    @Transactional
    public void on(final DispatcherNameUpdateEvent dispatcherNameUpdateEvent) throws UserNotFoundException {
        final UserEntity entity = this.repository.findByEmailAndRole(dispatcherNameUpdateEvent.getDispatcherEmail(), LogisticAppRole.DISPATCHER)
                                                 .orElseThrow(() -> new UserNotFoundException("User with email "+ dispatcherNameUpdateEvent.getDispatcherEmail() + " and role" + LogisticAppRole.DISPATCHER + " not found."));
        entity.updateName(dispatcherNameUpdateEvent.getDispatcherNameAfterUpdate());
        this.repository.save(entity);
    }

    @Transactional
    LogisticsAppUser createUser(final @NonNull  String email, final @NonNull  String password, final LogisticAppRole userRole) throws UserExistsException, UserUnAuthorizedException, UserNotFoundException, UnableToCreateVerificationCodeException {
        if (userRole.equals(LogisticAppRole.ADMIN)) {
            throw new UserUnAuthorizedException("ADMIN user cannot be created");
        }
        final UserEntity entity = UserEntity.create(null, email, this.passwordEncoder.encode(password), userRole, false, DEFAULT_PROVIDER);
        try {
            this.repository.save(entity);
            final LogisticsAppUser appUser = new LogisticsAppUser(entity);
            this.eventPublisher.publishEvent(new UnverifiedUserCreatedEvent(appUser));
            return appUser;
        }
        catch (final DataIntegrityViolationException e) {
            throw new UserExistsException("User with email " + email + " already exists");
        }
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
    public LogisticsAppUser assignRoleForAuthUser(final @NonNull String username, final @NonNull LogisticAppRole logisticsAppRole) throws UserNotFoundException {
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
        return appUser;
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
