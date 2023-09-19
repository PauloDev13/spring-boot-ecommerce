package com.prmorais.ecommerce.dto;

import com.prmorais.ecommerce.entity.Address;
import com.prmorais.ecommerce.entity.Customer;
import com.prmorais.ecommerce.entity.Order;
import com.prmorais.ecommerce.entity.OrderItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @EqualsAndHashCode
public class PurchaseDTO {
  private Customer customer;
  private Address shippingAddress;
  private Address billingAddress;
  private Order order;
  private Set<OrderItem> orderItems;
}
