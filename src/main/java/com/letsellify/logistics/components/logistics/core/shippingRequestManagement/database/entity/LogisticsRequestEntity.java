package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.database.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.common.audit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;
import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsAgent;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsItem;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsVendor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:27
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class LogisticsRequestEntity extends Auditable {
    @Id
    private UUID id;

    private String shippingRequestId;

    @Embedded
    private LogisticsItem item;

    private String currentState;

    private String currentLga;

    private String shippingState;

    private String shippingLga;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal amountForShipping;

    private BigDecimal amountForStorage;

    @Enumerated(EnumType.STRING)
    private PaymentMethod modeOfPayment;

    // dispatcher info
    @Embedded
    private LogisticsDispatcher dispatcher;

    @Embedded
    private LogisticsVendor vendor;

    @Embedded
    private LogisticsAgent agent;

    // agent info

    @OneToMany(mappedBy = "logisticsRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LogisticsItemImageEntity> itemImages = new ArrayList<>();

    protected LogisticsRequestEntity() {
        super();
    }

    public static LogisticsRequestEntity getInstance(
      final String requestId,
      final String vendorEmail,
      final String vendorFirstName,
      final String vendorLastName,
      final String vendorPhone,
      final String itemName,
      final String itemDescription,
      final List<LogisticsItemImage> itemImages,
      final String currentState,
      final String currentLga,
      final String shippingState,
      final String shippingLga,
      final LocalDate startDate,
      final LocalDate endDate,
      final BigDecimal amountForShipping,
      final BigDecimal amountForStorage,
      final PaymentMethod modeOfPayment
    ) {
        final LogisticsRequestEntity entity = new LogisticsRequestEntity();
        entity.id = UUID.randomUUID();
        entity.shippingRequestId = requestId;
        entity.vendor = new LogisticsVendor(vendorEmail, vendorFirstName, vendorLastName, vendorPhone);
        entity.item = new LogisticsItem(itemName, itemDescription);
        entity.getItemImages()
              .addAll(
                itemImages
                  .stream()
                  .map(itemImage -> {
                      final LogisticsItemImageEntity imageEntity = itemImage.getEntity();
                      imageEntity.setLogisticsRequest(entity);
                      return imageEntity;
                  })
                  .toList()
              );
        entity.currentState = currentState;
        entity.currentLga = currentLga;
        entity.shippingState = shippingState;
        entity.shippingLga = shippingLga;
        entity.startDate = startDate;
        entity.endDate = endDate;
        entity.amountForShipping = amountForShipping;
        entity.amountForStorage = amountForStorage;
        entity.modeOfPayment = modeOfPayment;
        return entity;
    }
}
