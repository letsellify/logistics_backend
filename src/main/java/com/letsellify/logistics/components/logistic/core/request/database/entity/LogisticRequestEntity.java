package com.letsellify.logistics.components.logistic.core.request.database.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsStatus;
import com.letsellify.logistics.components.logistic.core.request.data.Receiver;

import jakarta.persistence.*;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

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

    private LocalDate agentReceivingDate;

    private LocalDate agentDeliveringDate;

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
      final ItemEntity itemEntity,
      final List<LogisticsItemImage> itemImages,
      final String pickUpState,
      final String pickUpLga,
      final String pickUpAddress,
      final Receiver receiver,
      final BigDecimal agentPay,
      final BigDecimal dispatcherPay,
      final BigDecimal totalSpending,
      final LocalDate dispatcherPickUpDate,
      final LocalDate dispatcherDeliveryDate,
      final LocalDate agentReceivingDate,
      final LocalDate agentDeliveringDate,
      final LocalDateTime requestDate,
      final LogisticsStatus status
    ) {
        final LogisticRequestEntity entity = new LogisticRequestEntity();
        entity.id = UUID.randomUUID();
        entity.shippingRequestId = requestId;
        entity.senderId = senderId;
        entity.itemEntity = itemEntity;
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
        entity.pickUpAddress = pickUpAddress;
        entity.receiver = receiver;
        entity.dispatcherPay = dispatcherPay;
        entity.agentPay = agentPay;
        entity.totalSpending = totalSpending;
        entity.dispatcherPickUpDate = dispatcherPickUpDate;
        entity.dispatcherDeliveryDate = dispatcherDeliveryDate;
        entity.agentReceivingDate = agentReceivingDate;
        entity.agentDeliveringDate = agentDeliveringDate;
        entity.requestDate = requestDate;
        entity.status = status;

        return entity;
    }
}
