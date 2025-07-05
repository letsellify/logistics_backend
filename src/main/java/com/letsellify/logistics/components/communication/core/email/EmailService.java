package com.letsellify.logistics.components.communication.core.email;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.request.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.user.core.verificationCode.database.entity.VerificationCodeEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:11:27
 */

@Component
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${logistics.admin.email}")
    private final String adminEmail;

    public EmailService(final JavaMailSender javaMailSender, @Value("${logistics.admin.email}") final String adminEmail) {
        this.mailSender = javaMailSender;
        this.adminEmail = adminEmail;
    }


    @Async
    public void sendVerificationEmail(final @NonNull String userEmail, final @NonNull String code) {
        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(userEmail);
            helper.setSubject("Your Verification Code");
            helper.setFrom("Letsellify-Verification@letsellify.com");

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

    @Async
    public void sendApprovalSuccessMail(final @NonNull String userEmail, final @NonNull LogisticAppRole logisticAppRole) {
        final String subject;
        if (logisticAppRole == LogisticAppRole.DISPATCHER) {
            subject = "Congratulations! You're Now a Logistic Dispatcher";
        } else if (logisticAppRole == LogisticAppRole.AGENT) {
            subject = "Congratulations! You're Now a Logistic Agent";
        } else {
            throw new IllegalArgumentException("Can only send this mail for dispatchers and agents");
        }

        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, false);

            // Send email to the approved user
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setFrom("approval@letsellify.com");

            // Email content
            helper.setText("""
            Hello,
            
            Congratulations! Your application has been approved.
            
            You are now officially a %s on Letsellify.
            
            You can now start working and enjoying all the benefits of your role.
            
            Thank you for choosing Letsellify.
            
            Best Regards,
            Letsellify Team
            """.formatted(logisticAppRole), false); // Plain text email

            this.mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send approval success email", e);
        }
    }

    @Async
    public void sendToAdminAwaitApprovalMail(final @NonNull String userMail, final String userName, final @NonNull LogisticAppRole role) {
        final String subject;

        if (role == LogisticAppRole.DISPATCHER) {
            subject = "Dispatcher Awaiting Approval";
        } else if (role == LogisticAppRole.AGENT) {
            subject = "Agent Awaiting Approval";
        } else {
            throw new IllegalArgumentException("Can only send this mail for dispatchers and agents");
        }

        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, false);

            // Send email to admin
            helper.setTo(this.adminEmail);
            helper.setSubject(subject);
            helper.setFrom("approval@letsellify.com");

            // Email content
            helper.setText("""
            Hello Admin,
            
            A new user is awaiting approval.
            
            User Details:
            - Name: %s
            - Email: %s
            - Role: %s
            
            Please review and approve their account.
            
            Thank you,
            Letsellify Team
            """.formatted(userName, userMail, role), false); // Plain text email

            this.mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send approval email to admin", e);
        }
    }


    @Async
    public void sendLogisticRequestedMailToDispatchers(final LogisticRequestBroadcast broadcast, final Set<LogisticDispatcher> dispatchers) {
        for (final LogisticDispatcher dispatcher : dispatchers) {
            this.sendLogisticRequestEmail(dispatcher.email(), dispatcher.name(), broadcast, "https://letsellify.com/dispatcher-dashboard");
        }
    }


    @Async
    public void sendLogisticRequestedMailToAgents(final LogisticRequestBroadcast broadcast, final Set<LogisticAgent> agents) {
        for (final LogisticAgent agent : agents) {
            this.sendLogisticRequestEmail(agent.getEmail(), agent.getName(), broadcast, "https://letsellify.com/agent-dashboard");
        }
    }

    private void sendLogisticRequestEmail(final String email, final String name, final LogisticRequestBroadcast broadcast, final String dashboardUrl) {
        try {
            final MimeMessage message = this.mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("🚛 New Logistics Request Available!");

            final String emailContent = """
        <html>
        <body style="font-family: Arial, sans-serif; color: #333;">
            <p>Hello <strong>%s</strong>,</p>
            <p>A new logistics request has been posted and may require your attention.</p>

            <table style="width: 100%%; border-collapse: collapse;">
                <tr><td style="padding: 8px;"><strong>🆔 Request ID:</strong></td><td>%s</td></tr>
                <tr><td style="padding: 8px;"><strong>📦 Item:</strong></td><td>%s</td></tr>
                <tr><td style="padding: 8px;"><strong>🚚 Pickup Location:</strong></td><td>%s, %s</td></tr>
                <tr><td style="padding: 8px;"><strong>🎯 Destination:</strong></td><td>%s, %s</td></tr>
                <tr><td style="padding: 8px;"><strong>💰 Shipping Fee:</strong></td><td>₦%,.2f</td></tr>
                <tr><td style="padding: 8px;"><strong>💰 Storage Fee:</strong></td><td>₦%,.2f</td></tr>
                <tr><td style="padding: 8px;"><strong>🕒 PickUp Date:</strong></td><td>%s</td></tr>
                <tr><td style="padding: 8px;"><strong>🕒 Delivery Date:</strong></td><td>%s</td></tr>
            </table>

            <p style="margin-top: 20px;">You can check more details and accept this request on your dashboard.</p>

            <p style="text-align: center;">
                <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px;">
                    👉 Login to Dashboard
                </a>
            </p>

            <p>Regards, <br> <strong>Letsellify Logistics Team</strong></p>
        </body>
        </html>
        """.formatted(
              name,
              broadcast.getRequestId(),
              broadcast.getItemName(),
              broadcast.getPickUpLga(), broadcast.getPickUpState(),
              broadcast.getReceiverLga(), broadcast.getReceiverState(),
              broadcast.getDispatcherPay(),
              broadcast.getAgentPay(),
              broadcast.getDispatcherPickUpDate(),
              broadcast.getDispatcherDeliveryDate(),
              dashboardUrl
            );

            helper.setText(emailContent, true); // Enable HTML format
            helper.setFrom("Logistics@letsellify.com");

            this.mailSender.send(message);
        } catch (final MessagingException e) {
            log.error("Failed to send logistics request email to: {}", email, e);
        }
    }
}
