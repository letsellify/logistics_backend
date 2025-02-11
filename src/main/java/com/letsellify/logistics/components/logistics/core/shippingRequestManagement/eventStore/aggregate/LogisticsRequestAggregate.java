package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.aggregate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.letsellify.logistics.components.logistics.commands.AcceptDispatchRequestCommand;
import com.letsellify.logistics.components.logistics.commands.AcceptStorageRequestCommand;
import com.letsellify.logistics.components.logistics.commands.LogisticsRequestCommand;
import com.letsellify.logistics.components.logistics.commands.TriggerSettlementCommand;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticsDispatcher;
import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsAgent;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsStatus;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events.DispatchAcceptedEvent;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events.LogisticsRequestedEvent;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events.LogisticsSettlementEvent;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events.StorageAcceptedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * @author AHMAD BUBA
 * Date:1/22/25
 * Time:18:41
 */

@Aggregate
public class LogisticsRequestAggregate {
    @AggregateIdentifier
    private String requestId;

    private PaymentMethod modeOfPayment;

    private BigDecimal amountForDispatcher;

    private BigDecimal amountForAgent;

    private BigDecimal totalLogisticsAmountAfterTax;

    private LogisticsDispatcher dispatcher;

    private LocalDateTime handedToDispatcher;

    private LocalDateTime collectedAsDispatcher;

    private LogisticsAgent agent;

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

    protected LogisticsRequestAggregate() {}

    @CommandHandler
    public LogisticsRequestAggregate(final LogisticsRequestCommand command) {
        // Business rules validation
        //        if (command.getImages().isEmpty()) {
        //            throw new IllegalArgumentException("At least one image is required for a transport request.");
        //        }
        // Emit the event
        apply(new LogisticsRequestedEvent(
          command.getRequestId(),
          command.getVendorEmail(),
          command.getVendorFirstName(),
          command.getVendorLastName(),
          command.getVendorPhone(),
          command.getItemName(),
          command.getDescription(),
          command.getAmountForShipping(),
          command.getAmountForStorage(),
          command.getTotalAmountAfterTax(),
          command.getModeOfPayment(),
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
    public void on(final LogisticsRequestedEvent event) {
        this.requestId = event.requestId();
        this.modeOfPayment = event.modeOfPayment();
        this.amountForDispatcher = event.amountForShipping();
        this.amountForAgent = event.amountForStorage();
        this.totalLogisticsAmountAfterTax = event.totalAmountAfterTax();
    }

    @CommandHandler
    public void on(final AcceptDispatchRequestCommand cmd) {
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
    public void on(final TriggerSettlementCommand cmd) {
        apply(new LogisticsSettlementEvent(
          this.requestId,
          this.agent,
          this.dispatcher,
          this.amountForAgent,
          this.amountForDispatcher,
          cmd.getTriggeredOn()
        ));
    }

    @EventSourcingHandler
    public void on(final LogisticsSettlementEvent event) {
        this.logisticsCompleted = event.getTimestamp();
    }
}
