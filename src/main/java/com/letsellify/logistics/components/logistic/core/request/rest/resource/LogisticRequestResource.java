package com.letsellify.logistics.components.logistic.core.request.rest.resource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.letsellify.logistics.components.logistic.core.request.database.entity.ItemEntity;

/**
 * @author AHMAD BUBA
 * Date:1/23/25
 * Time:08:04
 */


public record LogisticRequestResource(
  String shippingRequestId,
  ItemEntity item,
  BigDecimal amountForShipping,
  BigDecimal amountForStorage,
  List<String> images,
  String currentState,
  String currentLga,
  String shippingState,
  String shippingLga,
  LocalDate startDate,
  LocalDate endDate
) {

}
