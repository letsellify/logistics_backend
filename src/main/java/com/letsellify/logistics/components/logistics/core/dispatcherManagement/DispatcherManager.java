package com.letsellify.logistics.components.logistics.core.dispatcherManagement;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.command.AcceptDispatchRequestCommand;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.repository.DispatcherRepository;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.kycManagement.KycManager;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.LogisticsKyc;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.event.ShippingRequestBroadcast;
import com.letsellify.logistics.components.user.core.userManagement.event.UserOfRoleDispatcherCreated;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:03
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class DispatcherManager {
    private final DispatcherRepository dispatcherRepository;
    private final KycManager kycManager;
    private final CommandGateway commandGateway;

    @Async
    @EventListener
    @Transactional
    public void on(final @NonNull UserOfRoleDispatcherCreated event) {
        log.info("Handling DispatcherCreatedEvent for email: {}", event.getUserEmail());
        // Dispatcher-specific logic here, e.g., assigning dispatch regions
        final DispatcherEntity entity = DispatcherEntity.getInstance(event.getUserEmail());
        this.dispatcherRepository.save(entity);
    }

    // listen for this event in agent and dispatcher module
    // for dispatcher get all open availabilties that fall within the date, state and lga
    // returns a list. filter to make sure you only send to the dispatcher 1s
    // 1 email could be for 2 possible slots
    // a core logic
    // check vendors accepting at the moment,
    // check if the start and end date is same
    // if same, just check vendors available from that day up wards
    // else check
    // send them an email which forces them online
    // this is because the email could contain just basic things(location, no images)
    // but then when the try to access it online
    // it interacts directly with shippingRequestDataService
    @EventListener
    public void on(final ShippingRequestBroadcast broadcast) {
        log.info("we have got a shipping request order from: {}", broadcast.getVendorEmail());
//        final List<DispatcherEntity> dispatcherEntityList = this.dispatcherRepository
//                                                              .findAllByCurrentlyAcceptingDeliveryTrueAndEnabledTrue();
        // send email
    }

    @Transactional
    public LogisticsDispatcher makeAvailable(final @NonNull String email) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + email + " found"));
        entity.setCurrentlyAcceptingDelivery(true);
        this.dispatcherRepository.save(entity);
        return new LogisticsDispatcher(entity);
    }

    @Transactional
    public LogisticsDispatcher makeUnavailable(final @NonNull String email) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + email + " found"));
        entity.setCurrentlyAcceptingDelivery(false);
        this.dispatcherRepository.save(entity);
        return new LogisticsDispatcher(entity);
    }

    @Transactional
    public LogisticsKyc uploadKycDocument(final @NonNull String userEmail, final @NonNull KycDocumentType kycDocumentType, final @NonNull MultipartFile multipartFile) throws NoSuchDispatcherException, LogisticsS3IOException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + userEmail + " found"));
        return this.kycManager.uploadKyc(entity.getEmail(), LogisticsAppRole.DISPATCHER, kycDocumentType, multipartFile);
    }

    @Transactional
    public void deleteKyc(final @NonNull String userEmail, final @NonNull String kycId) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + userEmail + " found"));
        this.kycManager.deleteKyc(userEmail, kycId);

    }

    public void acceptLogisticsRequest(final @NonNull String userEmail, final @NonNull String logisticsId) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + userEmail + " found"));
        final LogisticsDispatcher dispatcher = new LogisticsDispatcher(entity);
        final AcceptDispatchRequestCommand cmd = new AcceptDispatchRequestCommand(logisticsId, dispatcher);

    }

}
