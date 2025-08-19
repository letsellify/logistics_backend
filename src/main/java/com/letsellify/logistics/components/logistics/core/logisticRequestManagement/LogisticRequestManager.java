package com.letsellify.logistics.components.logistics.core.logisticRequestManagement;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistics.core.agentManagement.AgentManager;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.Agent;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherManager;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.InCompleteDispatcherProfileException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.FinanceAccountManager;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticAgent;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticRequest;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticRequests;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticsStatus;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.Receiver;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.Sender;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.ConditionEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.ItemEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.LogisticItemImageEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity.LogisticRequestEntity;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.repository.ConditionRepository;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.repository.LogisticItemImageRepository;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.repository.LogisticRequestRepository;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.command.LogisticInDispatcherPossessionCommand;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.command.LogisticRequestCommand;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.event.InDispatcherPossessionEvent;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.event.LogisticRequestedEvent;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.ImageConflictException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.InvalidLogisticItemImageException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.InvalidRoleException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.LogisticFraudException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.LogisticRequestAccessDeniedException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.NoSuchLogisticRequestException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.NigeriaStatesManager;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.VendorManager;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.Vendor;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.InCompleteVendorProfileException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:19
 */

@Component
@CacheConfig(cacheNames = "logistic_requests")
@Slf4j
public class LogisticRequestManager {
    private final LogisticRequestRepository logisticsRequestRepository;
    private final LogisticItemImageRepository logisticsItemImageRepository;
    private final CommandGateway commandGateway;
    private final NigeriaStatesManager nigeriaStatesManager;
    private final DispatcherManager dispatcherManager;
    private final VendorManager vendorManager;
    private final AgentManager agentManager;
    private final ConditionRepository conditionRepository;
    private final RedisTemplate<String, LogisticRequestEntity> requestCache;
    private final ApplicationEventPublisher eventPublisher; // in the case we become a microservice, this becomes a queue, so other components listen
    private final FinanceAccountManager financeAccountManager;  // tight dependency
    private final FileStorageManager fileStorageManager;
    private final static BigDecimal PROFIT_PERCENT = new BigDecimal(12);
    private final static BigDecimal HUNDRED = new BigDecimal(100);

    public LogisticRequestManager(
            final LogisticRequestRepository logisticsRequestRepository,
            final LogisticItemImageRepository logisticsItemImageRepository,
            final CommandGateway commandGateway,
            final NigeriaStatesManager nigeriaStatesManager,
            final DispatcherManager dispatcherManager,
            final VendorManager vendorManager,
            final AgentManager agentManager,
            final ConditionRepository conditionRepository,
            @Qualifier("logisticRequestRedisTemplate") final RedisTemplate<String, LogisticRequestEntity> requestCache,
            final ApplicationEventPublisher eventPublisher,
            final FinanceAccountManager financeAccountManager,
            final FileStorageManager fileStorageManager
    ) {
        this.logisticsRequestRepository = logisticsRequestRepository;
        this.logisticsItemImageRepository = logisticsItemImageRepository;
        this.commandGateway = commandGateway;
        this.nigeriaStatesManager = nigeriaStatesManager;
        this.dispatcherManager = dispatcherManager;
        this.vendorManager = vendorManager;
        this.agentManager = agentManager;
        this.conditionRepository = conditionRepository;
        this.requestCache = requestCache;
        this.eventPublisher = eventPublisher;
        this.financeAccountManager = financeAccountManager;
        this.fileStorageManager = fileStorageManager;
    }

    // consider caching here to improve speed
    private LogisticsItemImage uploadLogisticsItemImage(final @NonNull UUID senderId, final @NonNull String senderUsername, final @NonNull MultipartFile image) throws IOException {
        final String filePath = this.fileStorageManager.storeFile(StorageType.LOGISTICS, senderUsername, image);
        final LogisticItemImageEntity logisticsItemImageEntity = new LogisticItemImageEntity(senderId, filePath);
        this.logisticsItemImageRepository.save(logisticsItemImageEntity);
        return new LogisticsItemImage(logisticsItemImageEntity);
    }

    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull String userName, final @NonNull LogisticAppRole userRole, final @NonNull MultipartFile image) throws IOException, InCompleteVendorProfileException, VendorNotFoundException, InvalidRoleException {
        this.fileStorageManager.validateImageFile(image);
        if (!userRole.equals(LogisticAppRole.VENDOR)) {
            throw new InvalidRoleException("Only Vendors can upload logistics images and make logistic requests");
        }
        Vendor sender = this.vendorManager.findAndValidateVendor(userName);
        return uploadLogisticsItemImage(sender.getId(), sender.getEmail(), image);
    }

    public CompletableFuture<String> order(
            final @NonNull String vendorEmail,
            final @NonNull LogisticAppRole userRole,
            final @NonNull String itemName,
            final int quantity,
            final @NonNull String description,
            final String fragility,
            final @NonNull Set<String> condition,
            final int weight,
            final String imageUrl1,
            final String imageUrl2,
            final @NonNull String receiverFullName,
            final @NonNull String location,
            final @NonNull String state,
            final @NonNull String lga,
            final String email,
            final @NonNull String callPhoneNumber,
            final String whatsAppPhoneNumber,
            final @NonNull BigDecimal agentPay,
            final @NonNull BigDecimal dispatcherPay,
            final @NonNull LocalDate dispatcherPickUpDate,
            final @NonNull LocalDate dispatcherDeliveryDate,
            final @NonNull String pickUpState,
            final @NonNull String pickUpLga,
            final @NonNull String pickUpAddress
    ) throws VendorNotFoundException, InvalidLogisticItemImageException, InsufficientFundsException, NoSuchStateException, IllegalLGAException, InCompleteVendorProfileException, ImageConflictException, InvalidRoleException {
        if (!userRole.equals(LogisticAppRole.VENDOR)) {
            throw new InvalidRoleException("User with " + userRole + " cannot request logistic");
        }
        final Vendor vendor = this.vendorManager.findAndValidateVendor(vendorEmail);
        return this.order(
                        vendor,
                        itemName,
                        quantity,
                        description,
                        fragility,
                        condition,
                        weight,
                        imageUrl1,
                        imageUrl2,
                        receiverFullName,
                        location,
                        state,
                        lga,
                        email,
                        callPhoneNumber,
                        whatsAppPhoneNumber,
                        agentPay,
                        dispatcherPay,
                        dispatcherPickUpDate,
                        dispatcherDeliveryDate,
                        pickUpState,
                        pickUpLga,
                        pickUpAddress
                );
    }


    private CompletableFuture<String> order(
            final @NonNull Vendor vendor,
            final @NonNull String itemName,
            final int quantity,
            final @NonNull String description,
            final String fragility,
            final @NonNull Set<String> conditions,
            final int weight,
            final String imageUrl1,
            final String imageUrl2,
            final @NonNull String receiverFullName,
            final @NonNull String location,
            final @NonNull String state,
            final @NonNull String lga,
            final String email,
            final @NonNull String callPhoneNumber,
            final String whatsAppPhoneNumber,
            final @NonNull BigDecimal agentPay,
            final @NonNull BigDecimal dispatcherPay,
            final @NonNull LocalDate dispatcherPickUpDate,
            final @NonNull LocalDate dispatcherDeliveryDate,
            final @NonNull String pickUpState,
            final @NonNull String pickUpLga,
            final @NonNull String pickUpAddress
    ) throws NoSuchStateException, IllegalLGAException, InsufficientFundsException, InvalidLogisticItemImageException, ImageConflictException {
        /* Validate state and lga */

        if (!this.nigeriaStatesManager.validateStateLga(pickUpState,pickUpLga)) {
            throw new IllegalLGAException("LGA " + pickUpLga +  " does not belong to state " + pickUpState);
        }

        if (!this.nigeriaStatesManager.validateStateLga(state, lga)) {
            throw new IllegalLGAException("LGA " + lga +  " does not belong to state " +state);
        }


        final Set<String> images = new HashSet<>();
        images.add(imageUrl1);
        images.add(imageUrl2);

        final List<LogisticsItemImage> itemImages = new ArrayList<>();
        for (final String image : images) {
            if (image != null && !image.isBlank()) {
                final LogisticItemImageEntity imageEntity = this.logisticsItemImageRepository.findByIdAndSenderId(image, vendor.getId())
                        .orElseThrow(() -> new InvalidLogisticItemImageException("Image " + image + " not found or associated with" + vendor.getEmail()));
                if (imageEntity.getLogisticsRequest() != null) {
                    throw new ImageConflictException("This image belongs to another request");
                }
                itemImages.add(new LogisticsItemImage(imageEntity));
            }
        }

        final BigDecimal totalOrderAmount = dispatcherPay.add(agentPay);

        final BigDecimal totalSpendingAfterTax = totalOrderAmount
                .multiply(PROFIT_PERCENT)
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
        final BigDecimal totalSpending = totalOrderAmount.add(totalSpendingAfterTax);

        log.info("Total order amount " + totalOrderAmount);
        log.info("tax " + totalSpendingAfterTax);
        log.info("total spending after tax " + totalSpending);

        if (vendor.getBalance().compareTo(totalSpending) < 0) {
            throw new InsufficientFundsException("Insufficient balance to perform the transaction.");
        }


        final LogisticRequestCommand command = new LogisticRequestCommand(
                new Sender(vendor.getId(), vendor.getEmail(), vendor.getName(), vendor.getPhoneNumber(), vendor.getWhatsAppPhoneNumber()),
                itemName,
                quantity,
                description,
                fragility,
                conditions,
                weight,
                itemImages,
                receiverFullName,
                location,
                state,
                lga,
                email,
                callPhoneNumber,
                whatsAppPhoneNumber,
                agentPay,
                dispatcherPay,
                totalSpending,
                dispatcherPickUpDate,
                dispatcherDeliveryDate,
                dispatcherDeliveryDate,
                dispatcherDeliveryDate.plusDays(2),
                pickUpState,
                pickUpLga,
                pickUpAddress
        );

        // get current balance and update your balance

//        final List<String> imagesPreSignedUrl = itemImages
//                                                  .stream()
//                                                  .map(image -> this.fileStorageManager.generatePresignedUrl(image.getImageFilePath()))
//                                                  .toList();
        return this.commandGateway.send(command);

    }


    void setLogisticsToInDispatcherPossession(final @NonNull String dispatcherUsername, final @NonNull String shippingRequestId) throws NoSuchLogisticRequestException, NoSuchDispatcherException, LogisticFraudException {
        // First, check Redis cache
        final String cacheKey = "logistics:" + shippingRequestId;
        LogisticRequestEntity logisticRequestEntity = this.requestCache.opsForValue().get(cacheKey);

        // If not in cache, fetch from database
        if (logisticRequestEntity == null) {
            logisticRequestEntity = this.logisticsRequestRepository.findByShippingRequestId(shippingRequestId)
                    .orElseThrow(() -> new NoSuchLogisticRequestException("No logistic request found with this id " + shippingRequestId));

            // Cache it in Redis for future use
            this.requestCache.opsForValue().set(cacheKey, logisticRequestEntity);
        }

        final Dispatcher dispatcher = this.dispatcherManager.findDispatcher(dispatcherUsername);

        if (!logisticRequestEntity.getDispatcherId().equals(dispatcher.id())) {
            throw new LogisticFraudException("Unable to proceed, this logistic request does not belong to dispatcher");
        }
        final LogisticInDispatcherPossessionCommand cmd = new LogisticInDispatcherPossessionCommand(shippingRequestId);
        this.commandGateway.sendAndWait(cmd);
    }



    @Transactional
    public void writeLogisticRequestEvent(final LogisticRequestedEvent event) {
        try {
            /* should be vendor ID instead of email */
            this.financeAccountManager.escrowForLogistics(
                    event.getSender().getSenderId(),
                    LogisticAppRole.VENDOR,
                    event.getRequestId(),
                    event.getDispatcherPay(),
                    event.getAgentPay()
            );

        }
        // both exceptions must throw an exception to stop the program flow
        // send email to admin here. somethng sensitive might be wrong
        catch (final FinanceAccountNotFoundException e) {
            throw new RuntimeException(e);
        }
        // send an email to the vendor here saying due to insufficient funds
        // the order cannot go through
        // possibly accountManager should publish an event with the current balance
        // as the means an inconsistent homeState
        // vendor module listens and updates to the current balance
        catch (final InsufficientFundsException e) {
            throw new RuntimeException(e);
        }


        // this entity should accept a list of iamgees above. it sets the each image to have a reference to this: within the class
        final LogisticRequestEntity entity = LogisticRequestEntity.create(
                event.getRequestId(),
                event.getSender().getSenderId(),
                createItemFromRequest(event),
                event.getImages(),
                event.getPickUpState(),
                event.getPickUpLga(),
                event.getPickUpAddress(),
                new Receiver(event.getReceiverFullName(), event.getReceiverLocation(), event.getReceiverState(), event.getReceiverLga(), event.getReceiverEmail(), event.getReceiverCallNumber(), event.getReceiverWhatsAppNumber()),
                event.getAgentPay(),
                event.getDispatcherPay(),
                event.getTotalSpendingAfterTax(),
                event.getDispatcherPickUpDate(),
                event.getDispatcherDeliveryDate(),
                event.getAgentReceivingDate(),
                event.getAgentDeliveringDate(),
                event.getRequestDate(),
                LogisticsStatus.REQUESTED
        );

        this.logisticsRequestRepository.save(entity);

        final List<String> imagesPresignedUrls = entity.getItemImages()
                .stream()
                .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                .toList();

        final LogisticRequest request = new LogisticRequest(entity, imagesPresignedUrls, event.getSender(), null, null);

        this.eventPublisher.publishEvent(new LogisticRequestBroadcast(request));
    }


    @Transactional
    public void handle(final InDispatcherPossessionEvent event) throws NoSuchLogisticRequestException {
        final String cacheKey = "logistics:" + event.requestId();

        // Retrieve from cache
        LogisticRequestEntity logisticRequestEntity = this.requestCache.opsForValue().get(cacheKey);

        if (logisticRequestEntity == null) {
            // If not found in cache, fallback to DB (optional)
            logisticRequestEntity = this.logisticsRequestRepository.findByShippingRequestId(event.requestId())
                    .orElseThrow(() -> new NoSuchLogisticRequestException("No logistic request found with this id " + event.requestId()));
        }
        logisticRequestEntity.setDispatcherPossession(event.timestamp());
        this.logisticsRequestRepository.save(logisticRequestEntity);
        this.requestCache.opsForValue().set(cacheKey, logisticRequestEntity);
    }

    // whenever giving out a view to outside always poplute images list with
    // s3 presigned url instead
//    @QueryHandler
//    public LogisticRequest read(final @NonNull CompleteLogisticQuery query) throws NoSuchLogisticRequestException {
//        final LogisticRequestEntity entity = this.logisticsRequestRepository.findByShippingRequestId(query.getRequestId())
//                .orElseThrow(() -> new NoSuchLogisticRequestException("Wrong requestId, no record exists for: " + query.getRequestId()));
//        final List<String> imagesPresignedUrls = entity.getItemImages()
//                .stream()
//                .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
//                .toList();
//        return new LogisticRequest(entity, imagesPresignedUrls);
//    }


    public ItemEntity createItemFromRequest(LogisticRequestedEvent request) {
        Set<ConditionEntity> conditionEntities = request.getCondition().stream()
                .map(this::getOrCreateCondition)
                .collect(Collectors.toSet());

        return new ItemEntity(request.getItemName(), request.getQuantity(), request.getDescription(), request.getFragility(), request.getWeight(), conditionEntities);

    }

    private ConditionEntity getOrCreateCondition(String name) {
        return conditionRepository.findByName(name)
                .orElseGet(() -> conditionRepository.save(new ConditionEntity(name)));
    }


    public LogisticRequest getLogisticRequest(
            final @NonNull String userName,
            final @NonNull LogisticAppRole userRole,
            final @NonNull String shippingRequestId
    ) throws NoSuchLogisticRequestException, VendorNotFoundException, NoSuchAgentException,
            NoSuchDispatcherException, LogisticRequestAccessDeniedException, InvalidRoleException {

        LogisticRequestEntity entity = this.logisticsRequestRepository
                .findByShippingRequestId(shippingRequestId)
                .orElseThrow(() -> new NoSuchLogisticRequestException("No logistic request found with id: " + shippingRequestId));

        // Optional role-based access control
        switch (userRole) {
            case VENDOR -> {
                Vendor vendor = this.vendorManager.findVendor(userName);
                UUID vendorId = vendor.getId();
                if (entity.getSenderId() != null && !entity.getSenderId().equals(vendorId)) {
                    throw new LogisticRequestAccessDeniedException("This request does not belong to the vendor.");
                }
            }
            case AGENT -> {
                Agent agent = this.agentManager.findAgent(userName);
                UUID agentId = agent.id();
                if (entity.getAgentId() != null && !entity.getAgentId().equals(agentId)) {
                    throw new LogisticRequestAccessDeniedException("This request does not belong to the agent.");
                }
            }
            case DISPATCHER -> {
                Dispatcher dispatcher = this.dispatcherManager.findDispatcher(userName);
                UUID dispatcherId = dispatcher.id();
                if (entity.getDispatcherId() != null && !entity.getDispatcherId().equals(dispatcherId)) {
                    throw new LogisticRequestAccessDeniedException("This request does not belong to the dispatcher.");
                }
            }
            case ADMIN -> {
                // No access restriction for admin
            }
            default -> throw new InvalidRoleException("Unsupported role: " + userRole);
        }

        // Build related role info if available
        Sender sender = null;
        LogisticAgent agent = null;
        LogisticDispatcher dispatcher = null;

        if (entity.getSenderId() != null) {
            sender = new Sender(this.vendorManager.findVendor(entity.getSenderId()));
        }
        if (entity.getAgentId() != null) {
            agent = new LogisticAgent(this.agentManager.findAgent(entity.getAgentId()));
        }
        if (entity.getDispatcherId() != null) {
            dispatcher = new LogisticDispatcher(this.dispatcherManager.findDispatcher(entity.getDispatcherId()));
        }

        return new LogisticRequest(entity, getPresignedImageUrls(entity), sender, dispatcher, agent);
    }


    public LogisticRequests getLogisticRequests(
            final @NonNull String userName,
            final @NonNull LogisticAppRole userRole,
            final @NonNull Pageable pageable
    ) throws VendorNotFoundException, InCompleteVendorProfileException, InvalidRoleException, NoSuchAgentException, NoSuchDispatcherException, InCompleteDispatcherProfileException {
        Page<LogisticRequestEntity> page;

        switch (userRole) {
            case VENDOR:
                Vendor vendor = this.vendorManager.findAndValidateVendor(userName);
                page = this.logisticsRequestRepository.findAllBySenderId(vendor.getId(), pageable);
                List<LogisticRequest> vendorRequests = page.getContent()
                        .stream()
                        .map(entity -> new LogisticRequest(
                                entity,
                                entity.getItemImages()
                                        .stream()
                                        .map(imageEntity -> this.fileStorageManager.generatePresignedUrl(imageEntity.getImageFilePath()))
                                        .collect(Collectors.toList()),
                                new Sender(vendor),
                                null,
                                null
                        ))
                        .toList();
                return new LogisticRequests(vendorRequests, page);

            case AGENT:
                Agent agent = this.agentManager.findAndValidateAgent(userName);
                page = this.logisticsRequestRepository.findAllByAgentId(agent.id(), pageable);
                List<LogisticRequest> agentRequests = page.getContent()
                        .stream()
                        .map(entity -> new LogisticRequest(
                                entity,
                                entity.getItemImages()
                                        .stream()
                                        .map(imageEntity -> this.fileStorageManager.generatePresignedUrl(imageEntity.getImageFilePath()))
                                        .collect(Collectors.toList()),
                                null,
                                null,
                                new LogisticAgent(agent)
                        ))
                        .toList();
                return new LogisticRequests(agentRequests, page);

            case DISPATCHER:
                Dispatcher dispatcher = this.dispatcherManager.findDispatcher(userName);
                page = this.logisticsRequestRepository.findAllByDispatcherId(dispatcher.id(), pageable);
                List<LogisticRequest> dispatcherRequests = page.getContent()
                        .stream()
                        .map(entity -> new LogisticRequest(
                                entity,
                                entity.getItemImages()
                                        .stream()
                                        .map(imageEntity -> this.fileStorageManager.generatePresignedUrl(imageEntity.getImageFilePath()))
                                        .collect(Collectors.toList()),
                                null,
                                new LogisticDispatcher(dispatcher),
                                null
                        ))
                        .toList();
                return new LogisticRequests(dispatcherRequests, page);

            default:
                throw new InvalidRoleException("Unsupported role: " + userRole);
        }
    }


//    public LogisticRequests getVendorLogisticRequests(final @NonNull UUID senderId, final @NonNull Pageable pageable) {
//        Page<LogisticRequestEntity> entityPage = this.logisticsRequestRepository.findAllBySenderId(senderId, pageable);
//        List<LogisticRequestEntity> entityList = entityPage.getContent();
//        List<LogisticRequest> dataList = entityList.stream().map(entity -> {
//            List<String> imagesPresignedUrls = entity
//                    .getItemImages()
//                    .stream()
//                    .map(logisticItemImageEntity -> this.fileStorageManager.generatePresignedUrl(logisticItemImageEntity.getImageFilePath()))
//                    .toList();
//            return new LogisticRequest(entity, imagesPresignedUrls);
//        }).toList();
//        return new LogisticRequests(dataList, entityPage);
//    }

    public LogisticRequest getDispatcherLogisticRequest(final @NonNull UUID dispatcherId, final @NonNull String logisticRequestId) {
        return null;
    }

    public LogisticRequest getAgentLogisticRequest(final @NonNull UUID agentId, final @NonNull String logisticRequestId) {
        return null;
    }

    private List<String> getPresignedImageUrls(LogisticRequestEntity entity) {
        return entity.getItemImages()
                .stream()
                .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                .collect(Collectors.toList());
    }

}
