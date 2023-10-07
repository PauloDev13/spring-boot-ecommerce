package com.prmorais.ecommerce.controller;

import com.prmorais.ecommerce.dto.PurchaseDTO;
import com.prmorais.ecommerce.dto.PurchaseResponseDTO;
import com.prmorais.ecommerce.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/checkout")
public class CheckoutController {
  private final CheckoutService checkoutService;

  public CheckoutController(CheckoutService checkoutService) {
    this.checkoutService = checkoutService;
  }

  @PostMapping("/purchase")
  public PurchaseResponseDTO placeOrder(@RequestBody PurchaseDTO purchaseDTO) {
    return checkoutService.placeOrder(purchaseDTO);
  }
}
