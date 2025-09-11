package com.letsellify.logistics.components.user.core.verificationCodeManagement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.emailManagement.EmailService;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.event.UnverifiedUserCreatedEvent;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.database.entity.VerificationCodeEntity;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.database.repository.VerificationCodeRepository;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.event.UserVerifiedEvent;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.InvalidVerificationCodeException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.UnableToCreateVerificationCodeException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:2/10/25
 * Time:10:33
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationCodeManager {
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserManager userManager;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;


    @Async
    @Transactional
    public void resendVerificationCode(final @NonNull String userEmail) throws UserNotFoundException, UnableToCreateVerificationCodeException {
        this.generateAndSendVerificationCode(userEmail,null);
    }


    @Transactional
    public void verifyCode(final @NonNull String userEmail, final @NonNull String code) throws InvalidVerificationCodeException {
        final Optional <VerificationCodeEntity> codeEntityOptional = this.verificationCodeRepository
                                                                       .findByUserEmailAndCodeAndExpiredFalseAndVerifiedFalse(userEmail, code);

        // throw exception instead
        if (codeEntityOptional.isEmpty()) {
            throw new InvalidVerificationCodeException("Code is invalid, expired, or already used"); // Code is invalid, expired, or already used
        }

        final VerificationCodeEntity verificationCode = codeEntityOptional.get();

        if (LocalDateTime.now().isAfter(verificationCode.getExpiresAt())) {
            // Mark as expired if the code has passed its expiry time
            verificationCode.setToExpired();
            this.verificationCodeRepository.save(verificationCode);
            throw new InvalidVerificationCodeException("Code is expired"); // Code is expired
        }

        final LogisticAppRole userRole = verificationCode.getRole();
        // Mark as verified
        verificationCode.setToVerified();
        this.verificationCodeRepository.save(verificationCode);
        this.eventPublisher.publishEvent(new UserVerifiedEvent(verificationCode.getUserEmail(), verificationCode.getRole()));
    }


    @Async
    @EventListener
    @Transactional
    public void on(final UnverifiedUserCreatedEvent unverifiedUserCreatedEvent) {
        try {
            this.verifyUser(unverifiedUserCreatedEvent.getEmail(), unverifiedUserCreatedEvent.getRole());
        }
        catch (UserNotFoundException | UnableToCreateVerificationCodeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }




    // Run daily at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanUpExpiredCodes() {
        final long start = System.currentTimeMillis();

        // Delete expired codes
        this.verificationCodeRepository.deleteExpiredCodes();

        final long duration = System.currentTimeMillis() - start;
        log.info("Expired verification codes cleaned up in {} ms", duration);
    }


    private void verifyUser(final @NonNull String userEmail, @NonNull final LogisticAppRole appRole) throws UserNotFoundException, UnableToCreateVerificationCodeException {
        this.generateAndSendVerificationCode(userEmail,appRole);
    }


    private void generateAndSendVerificationCode(final @NonNull String userEmail, final LogisticAppRole appRole) throws UserNotFoundException, UnableToCreateVerificationCodeException {
        final LogisticAppRole roleToUse;

        if (appRole == null) {
            final LogisticsAppUser user = this.userManager.getUserByEmail(userEmail);
            roleToUse = user.getRole();
        } else if (appRole == LogisticAppRole.ADMIN) {
            throw new UnableToCreateVerificationCodeException("Verification codes cannot be generated for ADMIN users.");
        } else {
            roleToUse = appRole;
        }

        // Invalidate existing unverified and unexpired codes
        this.verificationCodeRepository.deleteByUserEmailAndVerifiedFalseAndExpiredFalse(userEmail);

        // Create and save the new verification code entity
        final VerificationCodeEntity entity = new VerificationCodeEntity(userEmail, roleToUse);
        this.verificationCodeRepository.save(entity);

        log.info("Here is the verification code for user {}: {}", userEmail, entity.getCode());

        // Send the verification email
        this.emailService
          .sendVerificationEmail(userEmail, entity.getCode());
    }
}
