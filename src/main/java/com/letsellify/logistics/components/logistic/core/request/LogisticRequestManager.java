package com.letsellify.logistics.components.logistic.core.request;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.letsellify.logistics.components.logistic.core.request.database.entity.ConditionEntity;
import com.letsellify.logistics.components.logistic.core.request.database.repository.ConditionRepository;
import com.letsellify.logistics.components.logistic.core.request.exception.ImageConflictException;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.logistic.core.dispatcher.DispatcherManager;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.dispatcher.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistic.core.financeAccount.FinanceAccountManager;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistic.core.financeAccount.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.StateLGAManager;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.request.database.entity.ItemEntity;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticRequest;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsStatus;
import com.letsellify.logistics.components.logistic.core.request.data.Receiver;
import com.letsellify.logistics.components.logistic.core.request.data.Sender;
import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticItemImageEntity;
import com.letsellify.logistics.components.logistic.core.request.database.entity.LogisticRequestEntity;
import com.letsellify.logistics.components.logistic.core.request.database.repository.LogisticItemImageRepository;
import com.letsellify.logistics.components.logistic.core.request.database.repository.LogisticRequestRepository;
import com.letsellify.logistics.components.logistic.core.request.event.LogisticRequestBroadcast;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.LogisticInDispatcherPossessionCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.LogisticRequestCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.InDispatcherPossessionEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.LogisticRequestedEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.query.CompleteLogisticQuery;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.request.exception.InvalidLogisticItemImageException;
import com.letsellify.logistics.components.logistic.core.request.exception.LogisticFraudException;
import com.letsellify.logistics.components.logistic.core.request.exception.NoSuchLogisticRequestException;
import com.letsellify.logistics.components.logistic.core.vendor.data.Vendor;

import lombok.NonNull;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:19
 */

@Component
@Slf4j
public class LogisticRequestManager {
    private final LogisticRequestRepository logisticsRequestRepository;
    private final LogisticItemImageRepository logisticsItemImageRepository;
    private final CommandGateway commandGateway;
    private final StateLGAManager stateLGAManager;
    private final DispatcherManager dispatcherManager;
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
      final StateLGAManager stateLGAManager,
      final DispatcherManager dispatcherManager,
      final ConditionRepository conditionRepository,
      @Qualifier("logisticRequestRedisTemplate")
      final RedisTemplate<String, LogisticRequestEntity> requestCache,
      final ApplicationEventPublisher eventPublisher,
      final FinanceAccountManager financeAccountManager,
      final FileStorageManager fileStorageManager
    ) {
     this.logisticsRequestRepository = logisticsRequestRepository;
     this.logisticsItemImageRepository = logisticsItemImageRepository;
     this.commandGateway = commandGateway;
     this.stateLGAManager = stateLGAManager;
     this.dispatcherManager = dispatcherManager;
     this.conditionRepository = conditionRepository;
     this.requestCache = requestCache;
     this.eventPublisher = eventPublisher;
     this.financeAccountManager = financeAccountManager;
     this.fileStorageManager = fileStorageManager;
    }

    // consider caching here to improve speed
    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull UUID senderId, final @NonNull String senderUsername, final @NonNull MultipartFile image) throws IOException {
        final String filePath = this.fileStorageManager.storeFile(StorageType.LOGISTICS, senderUsername, image);
        final LogisticItemImageEntity logisticsItemImageEntity = new LogisticItemImageEntity(senderId, filePath);
        this.logisticsItemImageRepository.save(logisticsItemImageEntity);
        return new LogisticsItemImage(logisticsItemImageEntity);
    }


    public CompletableFuture<String> order(
      final @NonNull Vendor vendor,
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
    ) throws NoSuchStateException, IllegalLGAException, InsufficientFundsException, InvalidLogisticItemImageException, ImageConflictException {

        if (!this.stateLGAManager.validateStateAndLgaForLogistics(pickUpState, pickUpLga, state, lga)) {
            throw new IllegalLGAException("LGA does not belong to state");
        }

        final Set<String> images = Set.of(imageUrl1, imageUrl2);
        final List<LogisticsItemImage> itemImages = new ArrayList<>();

        for (final String image: images) {
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
          new Sender(vendor.getId(), vendor.getEmail(), vendor.getName(),vendor.getPhoneNumber(), vendor.getWhatsAppPhoneNumber()),
          itemName,
          quantity,
          description,
          fragility,
          condition,
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

        final LogisticDispatcher dispatcher = this.dispatcherManager.findDispatcher(dispatcherUsername);

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
          event.getRequestDate(),
          LogisticsStatus.REQUESTED
        );

        this.logisticsRequestRepository.save(entity);

        final List<String> imagesPresignedUrls = entity.getItemImages()
                                                       .stream()
                                                       .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                                                       .toList();

        final LogisticRequest request = new LogisticRequest(entity, imagesPresignedUrls);

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
    @QueryHandler
    public LogisticRequest read(final @NonNull CompleteLogisticQuery query) throws NoSuchLogisticRequestException {
        final LogisticRequestEntity entity = this.logisticsRequestRepository.findByShippingRequestId(query.getRequestId())
                                                                            .orElseThrow(() -> new NoSuchLogisticRequestException("Wrong requestId, no record exists for: " + query.getRequestId()));
        final List<String> imagesPresignedUrls = entity.getItemImages()
                                                       .stream()
                                                       .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                                                       .toList();
        return new LogisticRequest(entity,imagesPresignedUrls);
    }


    public ItemEntity createItemFromRequest(LogisticRequestedEvent request) {
        Set<ConditionEntity> conditionEntities = request.getCondition().stream()
                .map(this::getOrCreateCondition)
                .collect(Collectors.toSet());

        return new ItemEntity(request.getItemName(),request.getQuantity(),request.getDescription(), request.getFragility(), request.getWeight(), conditionEntities);

    }

    private ConditionEntity getOrCreateCondition(String name) {
        return conditionRepository.findByName(name)
                .orElseGet(() -> conditionRepository.save(new ConditionEntity(name)));
    }

    public LogisticRequest getVendorLogisticRequest(final @NonNull UUID senderId, final @NonNull String logisticRequestId) throws NoSuchLogisticRequestException {
        LogisticRequestEntity entity = this.logisticsRequestRepository.findByShippingRequestIdAndSenderId(logisticRequestId,senderId)
                .orElseThrow(() -> new NoSuchLogisticRequestException("No such request with id: " + logisticRequestId + " found for this sender"));
        return new LogisticRequest(entity, entity.getItemImages().stream().map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath())).collect(Collectors.toList()));
    }

    public List<LogisticRequest> getVendorLogisticRequests(final @NonNull UUID senderId) {
        List<LogisticRequestEntity> entityList = this.logisticsRequestRepository.findBySenderId(senderId);
        return entityList.stream().map(entity -> {
            List<String> imagesPresignedUrls = entity
                    .getItemImages()
                    .stream()
                    .map(logisticItemImageEntity -> this.fileStorageManager.generatePresignedUrl(logisticItemImageEntity.getImageFilePath()))
                    .toList();
            return new LogisticRequest(entity, imagesPresignedUrls);
        }).toList();
    }

    public LogisticRequest getDispatcherLogisticRequest(final @NonNull UUID dispatcherId, final @NonNull String logisticRequestId) {
        return null;
    }

    public LogisticRequest getAgentLogisticRequest(final @NonNull UUID agentId, final @NonNull String logisticRequestId) {
        return null;
    }
}
