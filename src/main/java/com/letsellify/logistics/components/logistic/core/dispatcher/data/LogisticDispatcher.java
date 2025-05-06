package com.letsellify.logistics.components.logistic.core.dispatcher.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsellify.logistics.components.logistic.core.dispatcher.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherResource;

import jakarta.persistence.Column;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:29
 */


public record LogisticDispatcher(
  @Column(name = "dispatcher_name")
  String name,
  @Column(name = "dispatcher_email")
  String email,
  @Column(name = "dispatcher_whatsAppPhone")
  @JsonProperty("whatApp_phone_number")
  String whatsAppPhone,
  @Column(name = "dispatcher_phone")
  String phone,
  @Column(name = "dispatcher_state")
  String state,
  @Column(name = "dispatcher_lga")
  String lga,
  @Column(name = "dispatcher_address")
  String address
) {

    public LogisticDispatcher(final DispatcherEntity dispatcherEntity) {
        this(
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
