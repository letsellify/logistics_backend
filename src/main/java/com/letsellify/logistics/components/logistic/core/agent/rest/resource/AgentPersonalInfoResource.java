package com.letsellify.logistics.components.logistic.core.agent.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:51
 */

public record AgentPersonalInfoResource(
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {}
