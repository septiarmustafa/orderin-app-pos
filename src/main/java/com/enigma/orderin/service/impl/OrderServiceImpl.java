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
import java.util.stream.Collectors;

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

        orderRepository.createNewOrder(order.getDate(), order.getCashier().getId());

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
    public OrderResponse getOrderResponseById(Integer id) {
        Optional<Order> order = orderRepository.getOrderById(id);
        if (order.isPresent()) {
            Order orders = order.get();
            OrderDetail orderDetail = orders.getOrderDetail().stream().findFirst().orElse(null);
            CashierResponse cashierResponse = cashierService.getById(orders.getCashier().getId());
            List<OrderDetailResponse> orderDetailResponses = orders.getOrderDetail()
                    .stream()
                    .map(orderDetails -> {
                        orderDetails.setOrder(orders);
                        ProductDetail currentProductDetail = orderDetails.getProductDetail();
                        return OrderDetailResponse.builder()
                                .orderDetailId(orderDetails.getId())
                                .quantity(orderDetails.getQuantity())
                                .product(ProductResponse.builder()
                                        .productId(currentProductDetail.getProduct().getId())
                                        .productName(currentProductDetail.getProduct().getName())
                                        .stock(currentProductDetail.getStock())
                                        .price(currentProductDetail.getPrice())
                                        .isActive(currentProductDetail.getIsActive())
                                        .build())
                                .build();
                    }).toList();
            if (orderDetail != null) {
                return OrderResponse.builder()
                        .orderId(orders.getId())
                        .transactionDate(orders.getDate())
                        .listOrderDetail(orderDetailResponses)
                        .cashier(cashierResponse)
                        .build();
            }
        }
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.getAllOrder();
        return  orders.stream()
                .map(order -> {
                    CashierResponse cashierResponse = cashierService.getById(order.getCashier().getId());
                    List<OrderDetailResponse> orderDetailResponses = order.getOrderDetail()
                            .stream()
                            .map(orderDetail -> {
                                orderDetail.setOrder(order);
                                ProductDetail productDetail = orderDetail.getProductDetail();
                                return OrderDetailResponse.builder()
                                        .orderDetailId(orderDetail.getId())
                                        .quantity(orderDetail.getQuantity())
                                        .product(ProductResponse.builder()
                                                .productId(productDetail.getProduct().getId())
                                                .productName(productDetail.getProduct().getName())
                                                .stock(productDetail.getStock())
                                                .price(productDetail.getPrice())
                                                .isActive(productDetail.getIsActive())
                                                .build())
                                        .build();
                            }).toList();
                    return  OrderResponse.builder()
                            .orderId(order.getId())
                            .cashier(cashierResponse)
                            .listOrderDetail(orderDetailResponses)
                            .transactionDate(order.getDate())
                            .build();

                }).collect(Collectors.toList());
    }
}
