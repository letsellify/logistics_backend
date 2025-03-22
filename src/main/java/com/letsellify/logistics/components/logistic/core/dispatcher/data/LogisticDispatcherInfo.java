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
  String state,
  String lga,
  String address,
  Map<KycDocumentType,String> kycs
) {

    public LogisticDispatcherInfo(final DispatcherEntity entity, final Map<KycDocumentType,String> kycs) {
        this(
          entity.getEmail(),
          entity.getName(),
          entity.getKycId(),
          entity.getPersonalInfo().getWhatsAppPhone(),
          entity.getPersonalInfo().getPhone(),
          entity.getPersonalInfo().getState(),
          entity.getPersonalInfo().getLga(),
          entity.getPersonalInfo().getAddress(),
          kycs
        );
    }

    public LogisticDispatcherInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }

}
