package com.enigma.orderin.service;

import com.enigma.orderin.dto.request.OrderRequest;
import com.enigma.orderin.dto.response.OrderResponse;
import com.enigma.orderin.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createNewOrder(OrderRequest orderRequest);
    Order getOrderById (Integer id);
    List<OrderResponse> getAllOrder();

}
