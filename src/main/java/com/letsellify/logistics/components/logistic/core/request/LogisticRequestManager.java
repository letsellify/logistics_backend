package com.letsellify.logistics.components.logistic.core.request;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsRequest;
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
public class LogisticRequestManager {
    private final LogisticRequestRepository logisticsRequestRepository;
    private final LogisticItemImageRepository logisticsItemImageRepository;
    private final CommandGateway commandGateway;
    private final StateLGAManager stateLGAManager;
    private final DispatcherManager dispatcherManager;
    private final RedisTemplate<String, LogisticRequestEntity> redisTemplate;
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
      @Qualifier("logisticRequestRedisTemplate")
      final RedisTemplate<String, LogisticRequestEntity> redisTemplate,
      final ApplicationEventPublisher eventPublisher,
      final FinanceAccountManager financeAccountManager,
      final FileStorageManager fileStorageManager
    ) {
     this.logisticsRequestRepository = logisticsRequestRepository;
     this.logisticsItemImageRepository = logisticsItemImageRepository;
     this.commandGateway = commandGateway;
     this.stateLGAManager = stateLGAManager;
     this.dispatcherManager = dispatcherManager;
     this.redisTemplate = redisTemplate;
     this.eventPublisher = eventPublisher;
     this.financeAccountManager = financeAccountManager;
     this.fileStorageManager = fileStorageManager;
    }

    // consider caching here to improve speed
    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull String vendorUsername, final @NonNull MultipartFile image) throws IOException {
        final String filePath = this.fileStorageManager.storeFile(StorageType.LOGISTICS, vendorUsername, image);
        final LogisticItemImageEntity logisticsItemImageEntity = new LogisticItemImageEntity(vendorUsername, filePath);
        this.logisticsItemImageRepository.save(logisticsItemImageEntity);
        return new LogisticsItemImage(logisticsItemImageEntity);
    }


    void setLogisticsToInDispatcherPossession(final @NonNull String dispatcherUsername, final @NonNull String shippingRequestId) throws NoSuchLogisticRequestException, NoSuchDispatcherException, LogisticFraudException {
        // First, check Redis cache
        final String cacheKey = "logistics:" + shippingRequestId;
        LogisticRequestEntity logisticRequestEntity = this.redisTemplate.opsForValue().get(cacheKey);

        // If not in cache, fetch from database
        if (logisticRequestEntity == null) {
            logisticRequestEntity = this.logisticsRequestRepository.findByShippingRequestId(shippingRequestId)
                                                                   .orElseThrow(() -> new NoSuchLogisticRequestException("No logistic request found with this id " + shippingRequestId));

            // Cache it in Redis for future use
            this.redisTemplate.opsForValue().set(cacheKey, logisticRequestEntity);
        }

        final LogisticDispatcher dispatcher = this.dispatcherManager.findDispatcher(dispatcherUsername);

        if (!logisticRequestEntity.getDispatcher().equals(dispatcher)) {
            throw new LogisticFraudException("Unable to proceed, this logistic request does not belong to dispatcher");
        }
        final LogisticInDispatcherPossessionCommand cmd = new LogisticInDispatcherPossessionCommand(shippingRequestId);
        this.commandGateway.sendAndWait(cmd);
    }



    public CompletableFuture<String> order(
      final @NonNull Vendor vendor,
      final @NonNull String itemName,
      final @NonNull String description,
      final @NonNull BigDecimal amountForShipping,
      final @NonNull BigDecimal amountForStorage,
      final @NonNull List<String> images,
      final @NonNull String currentState,
      final @NonNull String currentLga,
      final @NonNull String shippingState,
      final @NonNull String shippingLga,
      final @NonNull LocalDate possibleDeliveryDateStart,
      final @NonNull LocalDate possibleDeliveryDateEnd
    ) throws NoSuchStateException, IllegalLGAException, InsufficientFundsException, InvalidLogisticItemImageException
    {

        this.stateLGAManager.validateStateAndLgaForLogistics(currentState, currentLga, shippingState, shippingLga);

        final List<LogisticsItemImage> itemImages = new ArrayList<>();

        for (final String image: images) {
            final LogisticItemImageEntity imageEntity = this.logisticsItemImageRepository.findByIdAndVendorUsername(image, vendor.getEmail())
                                                                                         .orElseThrow(() -> new InvalidLogisticItemImageException("Image " + image + " not found or associated with" + vendor.getEmail()));
            itemImages.add(new LogisticsItemImage(imageEntity));

        }

        final BigDecimal totalOrderAmount = amountForShipping.add(amountForStorage);

        final BigDecimal totalSpendingAfterTax = totalOrderAmount
                                                   .multiply(PROFIT_PERCENT)
                                                   .divide(HUNDRED, 2, RoundingMode.HALF_UP);

        if (vendor.getBalance().compareTo(totalSpendingAfterTax) < 0) {
            throw new InsufficientFundsException("Insufficient balance to perform the transaction.");
        }


        final LogisticRequestCommand command = new LogisticRequestCommand(
          vendor.getEmail(),
          vendor.getVendorName(),
          vendor.getPhone(),
          itemName,
          description,
          amountForShipping,
          amountForStorage,
          totalSpendingAfterTax,
          itemImages,
          currentState,
          currentLga,
          shippingState,
          shippingLga,
          possibleDeliveryDateStart,
          possibleDeliveryDateEnd
        );

        // get current balance and update your balance

        final List<String> imagesPresignedUrls = itemImages
                                                   .stream()
                                                   .map(image -> this.fileStorageManager.generatePresignedUrl(image.getImageFilePath()))
                                                   .toList();
        return this.commandGateway.send(command);

    }


    @Transactional
    public void write(final LogisticRequestedEvent event) {
        try {
                this.financeAccountManager.escrowForLogistics(
                    event.getVendorEmail(),
                    LogisticAppRole.VENDOR,
                    event.getRequestId(),
                    event.getAmountForShipping(),
                    event.getAmountForStorage()
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
          event.getVendorEmail(),
          event.getVendorName(),
          event.getVendorPhone(),
          event.getItemName(),
          event.getDescription(),
          event.getImages(),
          event.getCurrentState(),
          event.getCurrentLga(),
          event.getShippingState(),
          event.getShippingLga(),
          event.getPossibleDeliveryDateStart(),
          event.getPossibleDeliveryDateEnd(),
          event.getAmountForShipping(),
          event.getAmountForStorage()
        );

        this.logisticsRequestRepository.save(entity);

        final List<String> imagesPresignedUrls = entity.getItemImages()
                                                       .stream()
                                                       .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                                                       .toList();

        final LogisticsRequest request = new LogisticsRequest(entity, imagesPresignedUrls);

        this.eventPublisher.publishEvent(new LogisticRequestBroadcast(request));
    }


    @Transactional
    public void handle(final InDispatcherPossessionEvent event) throws NoSuchLogisticRequestException {
        final String cacheKey = "logistics:" + event.requestId();

        // Retrieve from cache
        LogisticRequestEntity logisticRequestEntity = this.redisTemplate.opsForValue().get(cacheKey);

        if (logisticRequestEntity == null) {
            // If not found in cache, fallback to DB (optional)
            logisticRequestEntity = this.logisticsRequestRepository.findByShippingRequestId(event.requestId())
                                                                   .orElseThrow(() -> new NoSuchLogisticRequestException("No logistic request found with this id " + event.requestId()));
        }
        logisticRequestEntity.setDispatcherPossession(event.timestamp());
        this.logisticsRequestRepository.save(logisticRequestEntity);
        this.redisTemplate.opsForValue().set(cacheKey, logisticRequestEntity);
    }

    // whenever giving out a view to outside always poplute images list with
    // s3 presigned url instead
    @QueryHandler
    public LogisticsRequest read(final @NonNull CompleteLogisticQuery query) throws NoSuchLogisticRequestException {
        final LogisticRequestEntity entity = this.logisticsRequestRepository.findByShippingRequestId(query.getRequestId())
                                                                            .orElseThrow(() -> new NoSuchLogisticRequestException("Wrong requestId, no record exists for: " + query.getRequestId()));
        final List<String> imagesPresignedUrls = entity.getItemImages()
                                                       .stream()
                                                       .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                                                       .toList();
        return new LogisticsRequest(entity,imagesPresignedUrls);
    }

}
