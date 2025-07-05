package com.letsellify.logistics.components.logistic.core.request.database.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistic.core.request.data.Item;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsStatus;
import com.letsellify.logistics.components.logistic.core.request.data.Receiver;

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
public class LogisticRequestEntity extends Auditable {
    @Id
    private UUID id;

    private String shippingRequestId;

    @Embedded
    private Item item;

    private UUID senderId;

    private String pickUpState;

    private String pickUpLga;

    private String pickUpAddress;

    private UUID dispatcherId;

    private UUID agentId;

    @Embedded
    private Receiver receiver;

    private BigDecimal dispatcherPay;

    private BigDecimal agentPay;

    private BigDecimal totalSpending;

    private LocalDate dispatcherPickUpDate;

    private LocalDate dispatcherDeliveryDate;

    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private LogisticsStatus status;

    private LocalDateTime dispatcherPossession;

    @OneToMany(mappedBy = "logisticsRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LogisticItemImageEntity> itemImages = new ArrayList<>();

    protected LogisticRequestEntity() {
        super();
    }

    public static LogisticRequestEntity create(
      final String requestId,
      final UUID senderId,
      final Item item,
      final List<LogisticsItemImage> itemImages,
      final String pickUpState,
      final String pickUpLga,
      final Receiver receiver,
      final BigDecimal agentPay,
      final BigDecimal dispatcherPay,
      final BigDecimal totalSpending,
      final LocalDate dispatcherPickUpDate,
      final LocalDate dispatcherDeliveryDate,
      final LocalDateTime requestDate
    ) {
        final LogisticRequestEntity entity = new LogisticRequestEntity();
        entity.id = UUID.randomUUID();
        entity.shippingRequestId = requestId;
        entity.senderId = senderId;
        entity.item = item;
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
        entity.pickUpState = pickUpState;
        entity.pickUpLga = pickUpLga;
        entity.receiver = receiver;
        entity.dispatcherPay = dispatcherPay;
        entity.agentPay = agentPay;
        entity.totalSpending = totalSpending;
        entity.dispatcherPickUpDate = dispatcherPickUpDate;
        entity.dispatcherDeliveryDate = dispatcherDeliveryDate;
        entity.requestDate = requestDate;
        return entity;
    }
}
