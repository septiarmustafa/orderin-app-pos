package com.enigma.orderin.service.impl;

import com.enigma.orderin.dto.request.OrderDetailRequest;
import com.enigma.orderin.dto.request.OrderRequest;
import com.enigma.orderin.dto.response.CashierResponse;
import com.enigma.orderin.dto.response.OrderDetailResponse;
import com.enigma.orderin.dto.response.OrderResponse;
import com.enigma.orderin.dto.response.ProductResponse;
import com.enigma.orderin.entity.*;
import com.enigma.orderin.repository.OrderDetailRepository;
import com.enigma.orderin.repository.OrderRepository;
import com.enigma.orderin.service.CashierService;
import com.enigma.orderin.service.OrderService;
import com.enigma.orderin.service.ProductDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    final OrderRepository orderRepository;
    final CashierService cashierService;
    final ProductDetailService productDetailService;
    final OrderDetailRepository orderDetailRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewOrder(OrderRequest orderRequest) {
        CashierResponse cashierResponse = cashierService.getById(orderRequest.getCashierId());

        List<OrderDetail> orderDetails = orderRequest.getOrderDetailList().stream().map(orderDetailRequest -> {
            ProductDetail productDetail = productDetailService.getById(orderDetailRequest.getProductDetailId());
            return OrderDetail.builder()
                    .productDetail(productDetail)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).toList();

        Order order = Order.builder()
                .cashier(Cashier.builder()
                        .id(cashierResponse.getId())
                        .build())
                .date(LocalDate.now())
                .orderDetail(orderDetails)
                .build();

        Integer orderId = orderRepository.createNewOrder(order.getDate(), order.getCashier().getId());

        Integer lastInsertOrderId = orderRepository.getLastInsertedId();

        if (lastInsertOrderId  == 0) {
            throw new RuntimeException("Failed to create the order");
        }

       Order savedOrder = getOrderById(lastInsertOrderId);

        for (OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetailList()) {
            Integer productDetailId = orderDetailRequest.getProductDetailId();
            Integer quantity = orderDetailRequest.getQuantity();
           orderDetailRepository.createOrderDetail(lastInsertOrderId, productDetailId, quantity);
        }


        Integer lastInsertOrderDetailId = orderDetailRepository.getLastInsertedId();


        List<OrderDetailResponse> orderDetailResponses = savedOrder.getOrderDetail()
                .stream()
                .map(orderDetail -> {
                    orderDetail.setOrder(savedOrder);
                    System.out.println(savedOrder);

            ProductDetail currentProductDetail = orderDetail.getProductDetail();
            currentProductDetail.setStock(currentProductDetail.getStock() - orderDetail.getQuantity());
            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .product(ProductResponse.builder()
                            .productId(currentProductDetail.getProduct().getId())
                            .productName(currentProductDetail.getProduct().getName())
                            .stock(currentProductDetail.getStock())
                            .price(currentProductDetail.getPrice())
                            .isActive(currentProductDetail.getIsActive())
                            .build())
                    .build();
        }).toList();

        return OrderResponse.builder()
                .orderId(lastInsertOrderId)
                .transactionDate(order.getDate())
                .cashier(cashierResponse)
                .listOrderDetail(orderDetailResponses)
                .build();
    }

    @Override
    public Order getOrderById(Integer id) {
        Optional<Order> orderOptional = orderRepository.getOrderById(id);
        return orderOptional.orElse(null);
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        return null;
    }
}
