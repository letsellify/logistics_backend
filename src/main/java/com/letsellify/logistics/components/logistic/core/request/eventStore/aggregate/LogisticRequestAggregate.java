package com.letsellify.logistics.components.logistic.core.request.eventStore.aggregate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcher;
import com.letsellify.logistics.components.logistic.core.request.data.LogisticsStatus;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.AcceptDispatchRequestCommand;
import com.letsellify.logistics.components.logistic.core.request.eventStore.command.AcceptStorageRequestCommand;
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

    private BigDecimal amountForDispatcher;

    private BigDecimal amountForAgent;

    private BigDecimal totalLogisticsAmountAfterTax;

    private LogisticDispatcher dispatcher;

    private Instant handedToDispatcher;

    private LocalDateTime collectedAsDispatcher;

    private LogisticAgent agent;

    // dispatcher hands over not vendor here
    private LocalDateTime handedToAgent;

    private LocalDateTime collectedAsAgent;

    // receiver confirms this
    private LocalDateTime itemReceived;

    // agent confirms this: handed over: if manual.payment. agent has to collect the money. if payment is online. this will
    // trigger payment. if not it will only be triggered after admin trigger the payment
    private LocalDateTime itemCollected;

    private LocalDateTime logisticsCompleted;

    private LogisticsStatus status;


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
          command.getVendorEmail(),
          command.getVendorName(),
          command.getVendorPhone(),
          command.getItemName(),
          command.getDescription(),
          command.getAmountForShipping(),
          command.getAmountForStorage(),
          command.getTotalAmountAfterTax(),
          command.getImages(),
          command.getCurrentState(),
          command.getCurrentLga(),
          command.getShippingState(),
          command.getShippingLga(),
          command.getPossibleDeliveryDateStart(),
          command.getPossibleDeliveryDateEnd()
        ));
    }


    @EventSourcingHandler
    public void on(final LogisticRequestedEvent event) {
        this.requestId = event.getRequestId();
        this.amountForDispatcher = event.getAmountForShipping();
        this.amountForAgent = event.getAmountForStorage();
        this.totalLogisticsAmountAfterTax = event.getTotalAmountAfterTax();
        this.status = LogisticsStatus.REQUESTED;
    }

    @CommandHandler
    public void on(final AcceptDispatchRequestCommand cmd) {
        if (this.dispatcher != null) {
            throw new IllegalStateException("A dispatcher has all ready accepted this request");
        }
        apply(new DispatchAcceptedEvent(
          this.requestId,
          cmd.dispatcher()
        ));
    }

    @EventSourcingHandler
    public void on(final DispatchAcceptedEvent event) {
        this.dispatcher = event.dispatcher();
    }


    @CommandHandler
    public void on(final AcceptStorageRequestCommand cmd) {
        if (this.agent != null) {
            throw new IllegalStateException("An agent has all ready accepted this request");
        }
        apply(new StorageAcceptedEvent(
          this.requestId,
          cmd.agent()
        ));
    }

    @EventSourcingHandler
    public void on(final StorageAcceptedEvent event) {
        this.agent = event.agent();
    }


    @CommandHandler
    public void on(final LogisticInDispatcherPossessionCommand cmd) {
        if (this.status != LogisticsStatus.REQUESTED || this.dispatcher == null) {
            throw new IllegalStateException("Invalid state, for the status of the request");
        }
        apply(new InDispatcherPossessionEvent(
          this.requestId,
          cmd.timestamp()
        ));
    }

    @EventSourcingHandler
    public void on(final InDispatcherPossessionEvent event) {
        this.handedToDispatcher = event.timestamp();
        this.status = LogisticsStatus.IN_DISPATCHER_POSSESSION;
    }


    @CommandHandler
    public void on(final TriggerSettlementCommand cmd) {
        apply(new LogisticSettlementEvent(
          this.requestId,
          this.agent,
          this.dispatcher,
          this.amountForAgent,
          this.amountForDispatcher,
          cmd.getTriggeredOn()
        ));
    }

    @EventSourcingHandler
    public void on(final LogisticSettlementEvent event) {
        this.logisticsCompleted = event.timestamp();
    }
}
