package com.enigma.orderin.service;

import com.enigma.orderin.dto.request.OrderRequest;
import com.enigma.orderin.dto.response.OrderResponse;
import com.enigma.orderin.dto.response.ProductResponse;
import com.enigma.orderin.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponse createNewOrder(OrderRequest orderRequest);
    Order getOrderById (Integer id);
    OrderResponse getOrderResponseById (Integer id);
    List<OrderResponse> getAllOrder();
    Page<OrderResponse> getAllWithPagination (Integer page, Integer size, Integer cashierId);


}
