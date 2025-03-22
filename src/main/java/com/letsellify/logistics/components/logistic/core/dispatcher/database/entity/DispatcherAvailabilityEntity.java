package com.letsellify.logistics.components.logistic.core.dispatcher.database.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:08
 */


// an order is tied to an availability. Order -> availability -> dispatcher


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class DispatcherAvailabilityEntity extends Auditable {
    // allow this out of the system and make attaching a booking based on it
    // repository: getById
    @Id
    private UUID id;

    private String currentState;

    private String currentLga;

    private String nextState;

    private String nextLga;

    private LocalDate dateOfTravel;

    private boolean active;

//    @ManyToOne
//    @JoinColumn(name = "dispatcher_id")
//    private DispatcherEntity dispatcher;

    protected DispatcherAvailabilityEntity() {
        super();
    }

    protected static DispatcherAvailabilityEntity getInstance(
      final DispatcherEntity dispatcherEntity,
      final String currentState,
      final String currentLga,
      final String nextState,
      final String nextLga,
      final LocalDate dateOfTravel
    ) {
        final DispatcherAvailabilityEntity dispatcherAvailabilityEntity = new DispatcherAvailabilityEntity();
        dispatcherAvailabilityEntity.id = UUID.randomUUID();
        dispatcherAvailabilityEntity.currentState = currentState;
        dispatcherAvailabilityEntity.currentLga = currentLga;
        dispatcherAvailabilityEntity.nextState = nextState;
        dispatcherAvailabilityEntity.nextLga = nextLga;
        dispatcherAvailabilityEntity.dateOfTravel = dateOfTravel;
        dispatcherAvailabilityEntity.active = true;
//        dispatcherAvailabilityEntity.dispatcher = dispatcherEntity;
        return dispatcherAvailabilityEntity;
    }

}
