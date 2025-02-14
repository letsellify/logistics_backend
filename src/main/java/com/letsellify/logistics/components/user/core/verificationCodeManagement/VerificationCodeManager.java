package com.letsellify.logistics.components.user.core.verificationCodeManagement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.user.core.userManagement.UserManager;
import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;
import com.letsellify.logistics.components.user.core.userManagement.event.UnverifiedUserCreatedEvent;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.database.entity.VerificationCodeEntity;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.database.repository.VerificationCodeRepository;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.event.UserVerifiedEvent;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.InvalidVerificationCodeException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.UnableToCreateVerificationCodeException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
    private final JavaMailSender mailSender;
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

        final LogisticsAppRole userRole = verificationCode.getRole();
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
            this.verifyUser(unverifiedUserCreatedEvent.getUsername(),unverifiedUserCreatedEvent.getRole());
        }
        catch (UserNotFoundException | UnableToCreateVerificationCodeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public void sendVerificationEmail(final @NonNull String userEmail, final @NonNull String code) {
        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(userEmail);
            helper.setSubject("Your Verification Code");
            helper.setFrom("no-reply@letsellify.com");

            helper.setText("""
                Hello,
                
                Your verification code is: %s
                
                This code will expire in %d minutes. If you did not request this, please ignore this email.
                
                Thank you,
                Letsellify Team
                """.formatted(code, VerificationCodeEntity.CODE_EXPIRY_MINUTES), false); // Plain text email

            this.mailSender.send(message);
        } catch (final MessagingException e) {
            log.error("Failed to send verification email to {}: {}", userEmail, e.getMessage());
            // Consider retrying here or saving to a dead-letter queue for manual reprocessing
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


    private void verifyUser(final @NonNull String userEmail, @NonNull final LogisticsAppRole appRole) throws UserNotFoundException, UnableToCreateVerificationCodeException {
        this.generateAndSendVerificationCode(userEmail,appRole);
    }


    private void generateAndSendVerificationCode(final @NonNull String userEmail, final LogisticsAppRole appRole) throws UserNotFoundException, UnableToCreateVerificationCodeException {
        final LogisticsAppRole roleToUse;

        if (appRole == null) {
            final LogisticsAppUser user = this.userManager.getUserByEmail(userEmail);
            roleToUse = user.getRole();
        } else if (appRole == LogisticsAppRole.ADMIN) {
            throw new UnableToCreateVerificationCodeException("Verification codes cannot be generated for ADMIN users.");
        } else {
            roleToUse = appRole;
        }

        // Invalidate existing unverified and unexpired codes
        this.verificationCodeRepository.deleteByUserEmailAndVerifiedFalseAndExpiredFalse(userEmail);

        // Create and save the new verification code entity
        final VerificationCodeEntity entity = new VerificationCodeEntity(userEmail, roleToUse);
        this.verificationCodeRepository.save(entity);

        // Send the verification email
        this.sendVerificationEmail(userEmail, entity.getCode());
    }
}
