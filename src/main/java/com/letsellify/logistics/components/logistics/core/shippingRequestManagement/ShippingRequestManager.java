package com.letsellify.logistics.components.logistics.core.shippingRequestManagement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.common.data.LogisticsAppRole;
import com.letsellify.logistics.components.fileStorage.core.FileStorageManager;
import com.letsellify.logistics.components.fileStorage.core.data.StorageType;
import com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception.LogisticsS3IOException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.AccountManager;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.FinanceAccountNotFoundException;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.StateLGAManager;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.paymentManagement.exception.LogisticsInsufficientFundsException;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsRequest;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity.LogisticsItemImageEntity;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity.LogisticsRequestEntity;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.repository.LogisticsItemImageRepository;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.repository.LogisticsRequestRepository;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.event.ShippingRequestBroadcast;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.command.LogisticsRequestCommand;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.event.LogisticsRequestedEvent;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.exception.InvalidLogisticsItemImageException;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.exception.NoSuchLogisticRequestException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.VendorManager;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.Vendor;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;
import com.letsellify.logistics.components.logistics.query.CompleteLogisticQuery;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:19
 */

@Component
@RequiredArgsConstructor
public class ShippingRequestManager {
    private final LogisticsRequestRepository logisticsRequestRepository;
    private final LogisticsItemImageRepository logisticsItemImageRepository;
    private final CommandGateway commandGateway;
    private final VendorManager vendorManager;
    private final StateLGAManager stateLGAManager;
    // in the case we become a micorservice, this becomes a queue, so other components listen
    private final ApplicationEventPublisher eventPublisher;
    // tight dependency
    private final AccountManager financeAccountManager;
    private final FileStorageManager fileStorageManager;
    private static final String LOGISTICS_IMAGE_TYPE = "logistics_item_image";
    private final static BigDecimal PROFIT_PERCENT = new BigDecimal(12);
    private final static BigDecimal HUNDRED = new BigDecimal(100);

    // consider caching here to improve speed
    public LogisticsItemImage uploadLogisticsItemImage(final @NonNull String vendorUsername, final @NonNull MultipartFile image) throws LogisticsS3IOException {
        final String filePath = this.fileStorageManager.storeFile(StorageType.LOGISTICS, vendorUsername, LOGISTICS_IMAGE_TYPE, image);
        final LogisticsItemImageEntity logisticsItemImageEntity = new LogisticsItemImageEntity(vendorUsername, filePath);
        this.logisticsItemImageRepository.save(logisticsItemImageEntity);
        return new LogisticsItemImage(logisticsItemImageEntity);
    }

    public void order(
      final @NonNull String vendorEmail,
      final @NonNull String itemName,
      final @NonNull String description,
      final @NonNull BigDecimal amountForShipping,
      final @NonNull BigDecimal amountForStorage,
      final @NonNull PaymentMethod modeOfPayment,
      final @NonNull List<String> images,
      final @NonNull String currentState,
      final @NonNull String currentLga,
      final @NonNull String shippingState,
      final @NonNull String shippingLga,
      final @NonNull LocalDate possibleDeliveryDateStart,
      final @NonNull LocalDate possibleDeliveryDateEnd
    ) throws NoSuchStateException, IllegalLGAException, VendorNotFoundException, LogisticsInsufficientFundsException, InvalidLogisticsItemImageException {

        this.stateLGAManager.validateStateAndLgaForLogistics(currentState, currentLga, shippingState, shippingLga);

        final Vendor vendor = this.vendorManager.findVendor(vendorEmail);

        final BigDecimal totalOrderAmount = amountForShipping.add(amountForStorage);

        final BigDecimal totalSpendingAfterTax = totalOrderAmount
                                                   .multiply(PROFIT_PERCENT)
                                                   .divide(HUNDRED, 2, RoundingMode.HALF_UP);

        if (modeOfPayment == PaymentMethod.ONLINE && vendor.getBalance().compareTo(totalSpendingAfterTax) < 0) {
            throw new LogisticsInsufficientFundsException("Insufficient balance to perform the transaction.");
        }

        final List<LogisticsItemImage> itemImages = new ArrayList<>();

        for (final String image: images) {
            final LogisticsItemImageEntity imageEntity = this.logisticsItemImageRepository.findByIdAndVendorUsername(image,vendorEmail)
                                                                                          .orElseThrow(() -> new InvalidLogisticsItemImageException("Image " + image + " not found or associated with" + vendorEmail));
            itemImages.add(new LogisticsItemImage(imageEntity));

        }

        final LogisticsRequestCommand command = new LogisticsRequestCommand(
          vendorEmail,
          vendor.getFirstName(),
          vendor.getLastName(),
          vendor.getPhone(),
          itemName,
          description,
          amountForShipping,
          amountForStorage,
          totalSpendingAfterTax,
          modeOfPayment,
          itemImages,
          currentState,
          currentLga,
          shippingState,
          shippingLga,
          possibleDeliveryDateStart,
          possibleDeliveryDateEnd
        );

        this.commandGateway.send(command);
        // get current balance and update your balance

        final List<String> imagesPresignedUrls = itemImages
                                                   .stream()
                                                   .map(image -> this.fileStorageManager.generatePresignedUrl(image.getImagefilePath()))
                                                   .toList();
    }

    // logisticsVendor, logisticsItem
    // here check the mode of payment. if manual go ahead else if logistics account
    // pass the userEmail, and all relevent details so that you accountManager makes the debit

    // maybe make a check to make sure we dont have a request with that requestId.
    @EventHandler
    public void handle(final LogisticsRequestedEvent event) {
        if (event.modeOfPayment() == PaymentMethod.ONLINE) {

            try {

                this.financeAccountManager
                  .escrowForLogistics(
                    event.vendorEmail(),
                    LogisticsAppRole.VENDOR,
                    UUID.fromString(event.requestId()),
                    event.amountForShipping(),
                    event.amountForStorage()
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
            // as the means an inconsistent state
            // vendor module listens and updates to the current balance
            catch (final LogisticsInsufficientFundsException e) {
                throw new RuntimeException(e);
            }
        }


        // this entity should accept a list of iamgees above. it sets the each image to have a reference to this: within the class
        final LogisticsRequestEntity entity = LogisticsRequestEntity.getInstance(
          event.requestId(),
          event.vendorEmail(),
          event.vendorFirstName(),
          event.vendorLastName(),
          event.vendorPhone(),
          event.itemName(),
          event.description(),
          event.images(),
          event.currentState(),
          event.currentLga(),
          event.shippingState(),
          event.shippingLga(),
          event.possibleDeliveryDateStart(),
          event.possibleDeliveryDateEnd(),
          event.amountForShipping(),
          event.amountForStorage(),
          event.modeOfPayment()
        );

        this.logisticsRequestRepository.save(entity);

        final List<String> imagesPresignedUrls = entity.getItemImages()
                                                       .stream()
                                                       .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                                                       .toList();

        this.eventPublisher.publishEvent(new ShippingRequestBroadcast(new LogisticsRequest(entity,imagesPresignedUrls)));
    }

    // whenever giving out a view to outside always poplute images list with
    // s3 presigned url instead
    @QueryHandler
    public LogisticsRequest read(final @NonNull CompleteLogisticQuery query) throws NoSuchLogisticRequestException {
        final LogisticsRequestEntity entity = this.logisticsRequestRepository.findByShippingRequestId(query.getRequestId())
                                                                             .orElseThrow(() -> new NoSuchLogisticRequestException("Wrong requestId, no record exists for: " + query.getRequestId()));
        final List<String> imagesPresignedUrls = entity.getItemImages()
                                                       .stream()
                                                       .map(itemImage -> this.fileStorageManager.generatePresignedUrl(itemImage.getImageFilePath()))
                                                       .toList();
        return new LogisticsRequest(entity,imagesPresignedUrls);
    }

}
