package com.prmorais.ecommerce.service.impl;

import com.prmorais.ecommerce.dao.CustomerRepository;
import com.prmorais.ecommerce.dto.PurchaseDTO;
import com.prmorais.ecommerce.dto.PurchaseResponseDTO;
import com.prmorais.ecommerce.entity.Customer;
import com.prmorais.ecommerce.entity.Order;
import com.prmorais.ecommerce.entity.OrderItem;
import com.prmorais.ecommerce.service.CheckoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {
  private final CustomerRepository customerRepository;

  public CheckoutServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  @Transactional
  public PurchaseResponseDTO placeOrder(PurchaseDTO purchaseDTO) {
    // recupera as informações da ordem de compra vindas do DTO
    Order order = purchaseDTO.getOrder();

    // gera o número de rastreio
    String orderTrackingNumber = generateOrderTrackingNumber();
    // atribui o número de rastreio à ordem de compra
    order.setOrderTrackingNumber(orderTrackingNumber);

    // preenche a ordem de compra com os itens
    Set<OrderItem> orderItems = purchaseDTO.getOrderItems();
    orderItems.forEach(order::add);

    // preenche endereços de cobrança e entrega na ordem de compra
    order.setBillingAddress(purchaseDTO.getBillingAddress());
    order.setShippingAddress(purchaseDTO.getShippingAddress());

    // popula o objeto customer com os dados da ordem de compra
    Customer customer = purchaseDTO.getCustomer();
    // checa se já existe Cliente com o email informado
    String theEmail = customer.getEmail();
    Customer customerFromDB = customerRepository.findByEmail(theEmail);
    // se o houve cliente com o email
    if (customerFromDB != null) {
      // atualiza o cliente enviado com os dados do cliente encontrado no BD
      customer = customerFromDB;
    }
    customer.add(order);

    // salva no banco de dados
    customerRepository.save(customer);

    return new PurchaseResponseDTO(orderTrackingNumber);
  }

  // função para gerar número de rastreio
  private String generateOrderTrackingNumber() {
    return UUID.randomUUID().toString();
  }
}
