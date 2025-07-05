package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author AHMAD BUBA
 * Date:6/21/25
 * Time:18:00
 */

public record OtherDetail(
  BigDecimal agentPay,
  BigDecimal dispatcherPay,
  LocalDate dispatcherPickUpDate,
  LocalDate dispatcherDeliveryDate,
//  LocalDate agentReceivingDate,
//  LocalDate agentDeliveryDate,
  String pickUpState,
  String pickUpLga,
  String pickUpAddress
) {}
