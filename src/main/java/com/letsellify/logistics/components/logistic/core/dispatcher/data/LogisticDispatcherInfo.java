package com.letsellify.logistics.components.logistic.core.dispatcher.data;

import java.util.Map;
import java.util.UUID;

import com.letsellify.logistics.components.logistic.core.dispatcher.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistic.core.dispatcher.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:10:30
 */

public record LogisticDispatcherInfo(
  String email,
  String name,
  UUID kycId,
  String whatsAppPhone,
  String phone,
  String homeState,
  String homeLga,
  String homeAddress,
  Map<KycDocumentType,String> kycs
) {

    public LogisticDispatcherInfo(final DispatcherEntity entity, final Map<KycDocumentType,String> kycs) {
        this(
          entity.getEmail(),
          entity.getPersonalInfo().getName(),
          entity.getKycId(),
          entity.getContactInfo().getWhatsAppPhone(),
          entity.getContactInfo().getPhone(),
          entity.getPersonalInfo().getHomeState(),
          entity.getPersonalInfo().getHomeLga(),
          entity.getPersonalInfo().getHomeAddress(),
          kycs
        );
    }

    public LogisticDispatcherInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }

}
