package com.letsellify.logistics.components.logistic.core.dispatcher.data;

import com.letsellify.logistics.components.logistic.core.dispatcher.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.DispatcherPersonalInfoResource;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:32
 */


public record DispatcherInfo(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {


    public DispatcherInfo(final DispatcherEntity dispatcherEntity) {
        this(
          dispatcherEntity.getContactInfo().getWhatsAppPhone(),
          dispatcherEntity.getContactInfo().getPhone(),
          dispatcherEntity.getPersonalInfo().getHomeState(),
          dispatcherEntity.getPersonalInfo().getHomeLga(),
          dispatcherEntity.getPersonalInfo().getHomeAddress()
        );
    }

    public DispatcherPersonalInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
