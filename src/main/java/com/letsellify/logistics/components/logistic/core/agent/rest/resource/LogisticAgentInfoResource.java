package com.letsellify.logistics.components.logistic.core.agent.rest.resource;

import java.util.Map;

import com.letsellify.logistics.components.logistic.core.kyc.data.KycDocumentType;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:03:47
 */

public record LogisticAgentInfoResource(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address,
  Map<KycDocumentType,String> kycs
) {}
