package com.letsellify.logistics.components.logistic.core.dispatcher;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.communication.core.email.EmailService;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.DispatcherInfo;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcherInfo;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.repository.DispatcherRepository;
import com.letsellify.logistics.components.logistic.core.dispatcher.event.DispatcherAwaitApproval;
import com.letsellify.logistics.components.logistic.core.dispatcher.event.DispatcherNameUpdateEvent;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.DispatcherApprovedException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.DispatcherExistsException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.UnapprovedDispatcherException;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto.DispatchDetailDto;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto.DispatcherContactInfoDto;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto.DispatcherPersonalInfoDto;
import com.letsellify.logistics.components.logistic.core.kyc.KycManager;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;
import com.letsellify.logistics.components.logistic.core.kyc.data.LogisticKycDocument;
import com.letsellify.logistics.components.logistic.core.kyc.data.LogisticKycs;
import com.letsellify.logistics.components.logistic.core.kyc.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.StateLGAManager;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.request.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.AcceptDispatchRequestCommand;
import com.letsellify.logistics.components.logistic.core.vendor.exception.VendorExistsException;
import com.letsellify.logistics.components.user.core.logisticUser.event.UserOfRoleDispatcherCreated;

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
    private final StateLGAManager stateLGAManager;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageManager fileStorageManager;
    private final EmailService emailService;

    @Async
    @EventListener
    @Transactional
    public void on(final @NonNull UserOfRoleDispatcherCreated event) throws VendorExistsException, DispatcherExistsException {
        log.info("Handling DispatcherCreatedEvent for email: {}", event.getUserEmail());
        // Dispatcher-specific logic here, e.g., assigning dispatch regions
        final String dispatcherEmail = event.getUserEmail();
        final String dispatcherName = event.getName();
        if (this.dispatcherRepository.existsByEmail(dispatcherEmail)) {
            throw new DispatcherExistsException("Dispatcher with email " + dispatcherEmail + " all ready exists");
        }
        final DispatcherEntity entity = DispatcherEntity.getInstance(dispatcherEmail,dispatcherName);
        this.dispatcherRepository.save(entity);
    }

    // listen for this event in agent and dispatcher module
    // for dispatcher get all open availabilties that fall within the date, homeState and homeLga
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
    public void on(final LogisticRequestBroadcast broadcast) {
        log.info("we have got a shipping request order from: {}", broadcast.getVendorEmail());
        final List<DispatcherEntity> dispatcherEntityList = this.dispatcherRepository.findByCurrentlyAcceptingDeliveryAndApprove(true,true);
        final Set<LogisticDispatcher> dispatchers = new HashSet<>();
        for (final DispatcherEntity entity: dispatcherEntityList) {
            dispatchers.add(new LogisticDispatcher(entity));
        }
        // send email
        this.emailService.sendLogisticRequestedMailToDispatchers(broadcast, dispatchers);
    }


    @Transactional
    public LogisticDispatcherInfo viewDispatcherPersonalInfo(final @NonNull String dispatcherEmail) throws NoSuchDispatcherException, NoKycRecordFoundException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + dispatcherEmail + " found"));
        final LogisticKycs kyc = this.kycManager.findDispatcherKyc(dispatcherEmail);
        return new LogisticDispatcherInfo(entity,this.getKycsPresignedUrl(kyc.getKycs()));
    }

    // send an email here telling the dispatcher he has been approved
    @Transactional
    public LogisticDispatcher approveDispatcher(final @NonNull String email) throws NoSuchDispatcherException, DispatcherApprovedException, NoKycRecordFoundException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + email + " found"));
        if (entity.isApprove()) {
            throw new DispatcherApprovedException("Dispatcher with email " + email + " all ready approved");
        }
        this.kycManager.approveKyc(entity.getKycId());
        entity.approve();
        this.dispatcherRepository.save(entity);
        this.emailService.sendApprovalSuccessMail(email, LogisticAppRole.DISPATCHER);
        return new LogisticDispatcher(entity);
    }

    //internal method
    public LogisticDispatcher findDispatcher(final @NonNull String dispatcherUsername) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherUsername)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + dispatcherUsername + " found"));
        return new LogisticDispatcher(entity);
    }

    LogisticDispatcherInfo viewPersonalInfoAfterApproval(final @NonNull String dispatcherEmail) throws NoSuchDispatcherException, NoKycRecordFoundException, UnapprovedDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(dispatcherEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + dispatcherEmail + " found"));
        if (!entity.isApprove()) {
            throw new UnapprovedDispatcherException("You have not been approved");
        }
        final LogisticKycs kyc = this.kycManager.findKyc(entity.getKycId());
        return new LogisticDispatcherInfo(entity,this.getKycsPresignedUrl(kyc.getKycs()));
    }

    @Transactional
    LogisticKycDocument uploadKycDocument(final @NonNull String userEmail, final @NonNull KycDocumentType kycDocumentType, final @NonNull MultipartFile multipartFile) throws NoSuchDispatcherException, IOException, DispatcherApprovedException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + userEmail + " found"));
        if (entity.isApprove()) {
            throw new DispatcherApprovedException("Dispatcher with email " + userEmail + " all ready approved");
        }
        return this.kycManager.uploadKyc(entity.getEmail(), LogisticAppRole.DISPATCHER, kycDocumentType, multipartFile);
    }

    @Transactional(readOnly = true)
    void deleteKyc(final @NonNull String userEmail, final @NonNull String kycId) throws NoSuchDispatcherException, DispatcherApprovedException, NoKycRecordFoundException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + userEmail + " found"));
        if (entity.isApprove()) {
            throw new DispatcherApprovedException("Dispatcher with email " + userEmail + " all ready approved");
        }
        this.kycManager.deleteKyc(userEmail, kycId);
    }

    @Transactional
    public DispatcherInfo setInfo(
      final @NonNull String email,
      final @NonNull DispatcherPersonalInfoDto personalInfoDto,
      final @NonNull DispatcherContactInfoDto contactInfoDto,
      final @NonNull DispatchDetailDto dispatchDetailDto
    ) throws NoSuchDispatcherException, NoSuchStateException, IllegalLGAException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + email + " found"));
        final String dispatcherNameBeforeUpdate = entity.getPersonalInfo().getName();
        this.stateLGAManager.validateStateLga(personalInfoDto.state(),personalInfoDto.lga());
        this.stateLGAManager.validateStateLga(dispatchDetailDto.state(), dispatchDetailDto.lga());
        final DispatcherEntity.PersonalInfoEmbeddable personalInfoEmbeddable = new DispatcherEntity.PersonalInfoEmbeddable(personalInfoDto.name(), personalInfoDto.state(), personalInfoDto.lga(), personalInfoDto.address());
        final DispatcherEntity.ContactInfoEmbeddable contactInfoEmbeddable = new DispatcherEntity.ContactInfoEmbeddable(contactInfoDto.whatsAppPhone(),contactInfoDto.phone());
        final DispatcherEntity.DispatchDetailEmbeddable dispatchDetailEmbeddable = new DispatcherEntity.DispatchDetailEmbeddable(dispatchDetailDto.state(), dispatchDetailDto.lga(), dispatchDetailDto.identificationNumber());
        entity.setPersonalInfo(personalInfoEmbeddable);
        entity.setContactInfo(contactInfoEmbeddable);
        entity.setDispatchDetail(dispatchDetailEmbeddable);
        final String dispatcherNameAfterUpdate = entity.getPersonalInfo().getName();
        if (personalInfoDto.name() != null && !dispatcherNameBeforeUpdate.equals(dispatcherNameAfterUpdate)) {
            this.eventPublisher.publishEvent((new DispatcherNameUpdateEvent(entity.getEmail(), dispatcherNameBeforeUpdate, dispatcherNameAfterUpdate)));
        }
        this.dispatcherRepository.save(entity);
        return new DispatcherInfo(entity);
    }

    @Transactional
    LogisticDispatcherInfo confirmInfoSubmissionForApproval(final @NonNull String email) throws NoSuchDispatcherException, DispatcherApprovedException, NoKycRecordFoundException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + email + " found"));
        if (entity.isApprove()) {
            throw new DispatcherApprovedException("Dispatcher with email " + email + " all ready approved");
        }
        final LogisticKycs kyc = this.kycManager.findDispatcherKyc(email);
        entity.setKycId(kyc.getKycId());
        this.dispatcherRepository.save(entity);
        final Map<KycDocumentType,String> kycs = this.getKycsPresignedUrl(kyc.getKycs());
        final LogisticDispatcherInfo dispatcherInfo = new LogisticDispatcherInfo(entity, kycs);
        this.eventPublisher.publishEvent(new DispatcherAwaitApproval(dispatcherInfo));
        return dispatcherInfo;
    }


    @Transactional
    LogisticDispatcher setCurrentlyAcceptingDelivery(final @NonNull String email, final boolean status) throws NoSuchDispatcherException, UnapprovedDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(email)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + email + " found"));
        if (!entity.isApprove()) {
            throw new UnapprovedDispatcherException("You have not been approved");
        }
        entity.setCurrentlyAcceptingDelivery(true);
        this.dispatcherRepository.save(entity);
        return new LogisticDispatcher(entity);
    }

    private Map<KycDocumentType,String> getKycsPresignedUrl(final List<LogisticKycDocument> kycDocuments) {
        return kycDocuments
                 .stream()
                 .collect(Collectors
                            .toMap(
                              LogisticKycDocument::getKycDocument,
                              logisticsKyc -> this.fileStorageManager.generatePresignedUrl(logisticsKyc.getKycFilePath()
                              )
                            )
                 );
    }


    // mark for removal: should be withing shippingRequestManager
    public void acceptLogisticsRequest(final @NonNull String userEmail, final @NonNull String logisticsId) throws NoSuchDispatcherException {
        final DispatcherEntity entity = this.dispatcherRepository.findByEmail(userEmail)
                                                                 .orElseThrow(() -> new NoSuchDispatcherException("No such dispatcher with email " + userEmail + " found"));
        final LogisticDispatcher dispatcher = new LogisticDispatcher(entity);
        final AcceptDispatchRequestCommand cmd = new AcceptDispatchRequestCommand(logisticsId, dispatcher);

    }

}
