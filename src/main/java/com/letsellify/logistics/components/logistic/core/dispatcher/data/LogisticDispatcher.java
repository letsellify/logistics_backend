package com.letsellify.logistics.components.logistic.core.dispatcher.data;

import java.util.UUID;

import com.letsellify.logistics.components.logistic.core.dispatcher.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherResource;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:29
 */


public record LogisticDispatcher(
  UUID id,
  String name,
  String email,
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {

    public LogisticDispatcher(final DispatcherEntity dispatcherEntity) {
        this(
          dispatcherEntity.getId(),
          dispatcherEntity.getPersonalInfo()
                          .getName(),
          dispatcherEntity.getEmail(),
          dispatcherEntity.getContactInfo().getWhatsAppPhone(),
          dispatcherEntity.getContactInfo().getPhone(),
          dispatcherEntity.getPersonalInfo().getHomeState(),
          dispatcherEntity.getPersonalInfo().getHomeLga(),
          dispatcherEntity.getPersonalInfo().getHomeAddress()
        );
    }

    public LogisticDispatcherResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
