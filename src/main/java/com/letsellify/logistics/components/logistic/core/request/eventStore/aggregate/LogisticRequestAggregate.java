package com.letsellify.logistics.components.logistic.core.request.eventStore.aggregate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.letsellify.logistics.components.logistic.core.request.data.Item;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsItemImage;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsStatus;
import com.letsellify.logistics.components.logistic.core.request.data.Receiver;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.AcceptStorageRequestCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.DispatchRequestAcceptedCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.LogisticInDispatcherPossessionCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.LogisticRequestCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.TriggerSettlementCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.DispatchAcceptedEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.InDispatcherPossessionEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.LogisticRequestedEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.LogisticSettlementEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.StorageAcceptedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * @author AHMAD BUBA
 * Date:1/22/25
 * Time:18:41
 */

@Aggregate
public class LogisticRequestAggregate {
    @AggregateIdentifier
    private String requestId;

    private UUID senderId;

    private Item item;

    private Receiver receiver;

    private List<LogisticsItemImage> images;

    private BigDecimal agentPay;

    private BigDecimal dispatcherPay;

    private BigDecimal totalSpendingAfterTax;

    private LocalDate dispatcherPickUpDate;

    private LocalDate dispatcherDeliveryDate;

    private String pickUpState;

    private String pickUpLga;

    private String pickUpAddress;

    private LocalDateTime requestDate;

    private UUID dispatcherId;

    private UUID agentId;

    private LogisticsStatus status;

    private LocalDateTime handedToDispatcher;

    private LocalDateTime acceptedForDispatch;

    // dispatcher hands over not vendor here
    private LocalDateTime handedToAgent;

    private LocalDateTime collectedAsAgent;

    // receiver confirms this
    private LocalDateTime itemReceived;

    // agent confirms this: handed over: if manual.payment. agent has to collect the money. if payment is online. this will
    // trigger payment. if not it will only be triggered after admin trigger the payment
    private LocalDateTime itemCollected;

    private LocalDateTime logisticsCompleted;




    //have another field to track the status.

    protected LogisticRequestAggregate() {}

    @CommandHandler
    public LogisticRequestAggregate(final LogisticRequestCommand command) {
        // Business rules validation
        //        if (command.getImages().isEmpty()) {
        //            throw new IllegalArgumentException("At least one image is required for a transport request.");
        //        }
        // Emit the event
        apply(new LogisticRequestedEvent(
          command.getRequestId(),
          command.getSender(),
          command.getItemName(),
          command.getQuantity(),
          command.getDescription(),
          command.getFragility(),
          command.getCondition(),
          command.getWeight(),
          command.getImages(),
          command.getReceiverFullName(),
          command.getReceiverLocation(),
          command.getReceiverState(),
          command.getReceiverLga(),
          command.getReceiverEmail(),
          command.getReceiverCallNumber(),
          command.getReceiverWhatsAppNumber(),
          command.getAgentPay(),
          command.getDispatcherPay(),
          command.getTotalSpendingAfterTax(),
          command.getDispatcherPickUpDate(),
          command.getDispatcherDeliveryDate(),
          command.getPickUpState(),
          command.getPickUpLga(),
          command.getPickUpAddress(),
          command.getRequestDate()
              )
        );
    }


    @EventSourcingHandler
    public void onLogisticRequestedEvent(final LogisticRequestedEvent event) {
        this.requestId = event.getRequestId();
        this.senderId = event.getSender().getSenderId();
        this.item = new Item(event.getItemName(), event.getQuantity(), event.getDescription(), event.getFragility(), event.getCondition(), event.getWeight());
        this.images = event.getImages();
        this.receiver = new Receiver(event.getReceiverFullName(),event.getReceiverLocation(), event.getReceiverState(), event.getReceiverLga(), event.getReceiverEmail(), event.getReceiverCallNumber(), event.getReceiverWhatsAppNumber());
        this.agentPay = event.getAgentPay();
        this.dispatcherPay = event.getDispatcherPay();
        this.totalSpendingAfterTax = event.getTotalSpendingAfterTax();
        this.dispatcherPickUpDate = event.getDispatcherPickUpDate();
        this.dispatcherDeliveryDate = event.getDispatcherDeliveryDate();
        this.pickUpState = event.getPickUpState();
        this.pickUpLga = event.getPickUpLga();
        this.pickUpAddress = event.getPickUpAddress();
        this.status = LogisticsStatus.REQUESTED;
        this.requestDate = event.getRequestDate();
    }

    @CommandHandler
    public void onLogisticRequestedEvent(final DispatchRequestAcceptedCommand cmd) {
        if (this.dispatcherId != null) {
            throw new IllegalStateException("A dispatcher has all ready accepted this request");
        }
        apply(new DispatchAcceptedEvent(
          this.requestId,
          cmd.dispatcherId()
        ));
    }

    @EventSourcingHandler
    public void onLogisticRequestedEvent(final DispatchAcceptedEvent event) {
        this.dispatcherId = event.dispatcherId();
    }


    @CommandHandler
    public void onLogisticRequestedEvent(final AcceptStorageRequestCommand cmd) {
        if (this.agentId != null) {
            throw new IllegalStateException("An agent has all ready accepted this request");
        }
        apply(new StorageAcceptedEvent(
          this.requestId,
          cmd.agentId()
        ));
    }

    @EventSourcingHandler
    public void onLogisticRequestedEvent(final StorageAcceptedEvent event) {
        this.agentId = event.agentId();
    }


    @CommandHandler
    public void onLogisticRequestedEvent(final LogisticInDispatcherPossessionCommand cmd) {
        if (this.status != LogisticsStatus.REQUESTED || this.dispatcherId == null) {
            throw new IllegalStateException("Invalid homeState, for the status of the request");
        }
        apply(new InDispatcherPossessionEvent(
          this.requestId,
          cmd.timestamp()
        ));
    }

    @EventSourcingHandler
    public void onLogisticRequestedEvent(final InDispatcherPossessionEvent event) {
        this.handedToDispatcher = event.timestamp();
        this.status = LogisticsStatus.IN_DISPATCHER_POSSESSION;
    }


    @CommandHandler
    public void onLogisticRequestedEvent(final TriggerSettlementCommand cmd) {
        apply(new LogisticSettlementEvent(
          this.requestId,
          this.agentId,
          this.dispatcherId,
          this.agentPay,
          this.dispatcherPay,
          cmd.getTriggeredOn()
        ));
    }

    @EventSourcingHandler
    public void onLogisticRequestedEvent(final LogisticSettlementEvent event) {
        this.logisticsCompleted = event.timestamp();
    }
}
