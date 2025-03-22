package com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource;

import java.util.Map;

import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:10:54
 */

public record LogisticDispatcherInfoResource(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address,
  Map<KycDocumentType,String> kycs
) {}
