package com.letsellify.logistics.components.logistic.core.dispatcher.data;

import com.letsellify.logistics.components.logistic.core.dispatcher.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.DispatcherPersonalInfoResource;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:32
 */


public record DispatcherPersonalInfo(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {


    public DispatcherPersonalInfo(final DispatcherEntity.PersonalInfoEmbeddable personalInfoEmbeddable) {
        this(
          personalInfoEmbeddable.getWhatsAppPhone(),
          personalInfoEmbeddable.getPhone(),
          personalInfoEmbeddable.getState(),
          personalInfoEmbeddable.getLga(),
          personalInfoEmbeddable.getAddress()
        );
    }

    public DispatcherPersonalInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
