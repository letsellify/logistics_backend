package com.letsellify.logistics.components.user.core.userManagement;

import com.letsellify.logistics.components.logistics.core.agentManagement.data.AgentInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.DispatchersInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.InCompleteDispatcherProfileException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.emailManagement.EmailService;
import com.letsellify.logistics.components.logistics.core.agentManagement.AgentManager;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.Agent;
import com.letsellify.logistics.components.logistics.core.agentManagement.event.AgentAwaitApproval;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherManager;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticDispatcherInfo;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.event.DispatcherAwaitApproval;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.DispatcherApprovedException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.kycManagement.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.user.core.userManagement.config.AdminUserProperties;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUsers;
import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.userManagement.database.repository.UserRepository;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:2/20/25
 * Time:11:03
 */

@Component
@EnableConfigurationProperties({ AdminUserProperties.class})
@RequiredArgsConstructor
@Slf4j
public class AdminUserManager implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserProperties adminUserProperties;
    private final EmailService emailService;
    private final UserManager userManager;
    private final DispatcherManager dispatcherManager;
    private final AgentManager agentManager;

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        if (!this.userRepository.existsByEmail(this.adminUserProperties.email())) {
            log.info("Will create admin user, admin not found");
            log.info("Email, {}", this.adminUserProperties.email());
            log.info("Password, {}", this.adminUserProperties.password());
            this.create(this.adminUserProperties.name(), this.adminUserProperties.email(), this.adminUserProperties.password());
        }
    }


    @EventListener
    public void on(final DispatcherAwaitApproval event) {
        this.emailService
          .sendToAdminAwaitApprovalMail(event.getDispatcherEmail(),event.getDispatcherName(), event.getRole());
    }

    @EventListener
    public void on(final AgentAwaitApproval event) {
        this.emailService
          .sendToAdminAwaitApprovalMail(event.getAgentEmail(),event.getAgentName(), event.getRole());
    }

    LogisticDispatcherInfo viewDispatcherPersonalInfo(final @NonNull String dispatcherEmail) throws NoKycRecordFoundException, NoSuchDispatcherException {
        return this.dispatcherManager.viewDispatcherPersonalInfo(dispatcherEmail);
    }

//    Dispatcher approveDispatcher(final @NonNull String dispatcherEmail) throws NoSuchDispatcherException, DispatcherApprovedException, NoKycRecordFoundException {
//        return this.dispatcherManager.approveDispatcher(dispatcherEmail);
//    }

    Dispatcher approveDispatcher(final @NonNull String dispatcherEmail) throws NoSuchDispatcherException, InCompleteDispatcherProfileException, DispatcherApprovedException {
        return this.dispatcherManager.approveDispatcher(dispatcherEmail);
    }

    DispatchersInfo getAllDispatchersAwaitingApproval(final @NonNull Pageable pageable)  {
        return this.dispatcherManager.getAllAwaitingApproval(pageable);
    }

    AgentInfo viewAgentPersonalInfo(final @NonNull String agentEmail) throws NoSuchAgentException, NoKycRecordFoundException {
        return this.agentManager.viewAgentPersonalInfo(agentEmail);
    }

    Agent approveAgent(final @NonNull String agentEmail) throws NoKycRecordFoundException, NoSuchAgentException, AgentApprovedException {
        return this.agentManager.approveAgent(agentEmail);
    }


    LogisticsAppUser getUser(final @NonNull String userEmail) throws UserNotFoundException {
        return this.userManager.getUserByEmail(userEmail);
    }

    LogisticsAppUsers getAllUsers(final @NonNull Pageable page) {
        return this.userManager.getAllUsers(page);
    }


    private void create(final String name, final @NonNull String email, final @NonNull String password) {
        final UserEntity entity = UserEntity
                                    .create(name, email, this.passwordEncoder.encode(password), LogisticAppRole.ADMIN, true, null);
        this.userRepository.save(entity);
    }
    

}
