package com.prmorais.ecommerce.service;

import com.prmorais.ecommerce.dto.PurchaseDTO;
import com.prmorais.ecommerce.dto.PurchaseResponseDTO;

public interface CheckoutService {
  PurchaseResponseDTO placeOrder(PurchaseDTO purchaseDTO);
}
