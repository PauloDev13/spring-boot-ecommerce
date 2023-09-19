package com.prmorais.ecommerce.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class PurchaseResponseDTO {
  private final String orderTrackingNumber;
}
