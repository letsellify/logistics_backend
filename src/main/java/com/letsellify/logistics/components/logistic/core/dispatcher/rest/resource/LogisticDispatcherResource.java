package com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:12:02
 */

public record LogisticDispatcherResource(
  String name,
  String email,
  String whatsAppPhone,
  String phone,
  String state,
  String lga,
  String address
) {}
