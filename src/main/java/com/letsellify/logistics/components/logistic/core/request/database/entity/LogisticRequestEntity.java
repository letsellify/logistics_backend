package com.letsellify.logistics.components.logistic.core.request.database.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItem;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsVendor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
public class LogisticRequestEntity extends Auditable {
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

    // dispatcher info
    @Embedded
    private LogisticDispatcher dispatcher;

    @Embedded
    private LogisticsVendor vendor;

    @Embedded
    private LogisticAgent agent;

    private Instant dispatcherPossession;

    // agent info

    @OneToMany(mappedBy = "logisticsRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LogisticItemImageEntity> itemImages = new ArrayList<>();

    protected LogisticRequestEntity() {
        super();
    }

    public static LogisticRequestEntity create(
      final String requestId,
      final String vendorEmail,
      final String vendorName,
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
      final BigDecimal amountForStorage
    ) {
        final LogisticRequestEntity entity = new LogisticRequestEntity();
        entity.id = UUID.randomUUID();
        entity.shippingRequestId = requestId;
        entity.vendor = new LogisticsVendor(vendorEmail, vendorName, vendorPhone);
        entity.item = new LogisticsItem(itemName, itemDescription);
        entity.getItemImages()
              .addAll(
                itemImages
                  .stream()
                  .map(itemImage -> {
                      final LogisticItemImageEntity imageEntity = itemImage.getEntity();
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
        return entity;
    }
}
