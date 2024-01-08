package com.enigma.orderin.service.impl;

import com.enigma.orderin.dto.response.OrderDetailResponse;
import com.enigma.orderin.dto.response.ProductResponse;
import com.enigma.orderin.entity.OrderDetail;
import com.enigma.orderin.repository.OrderDetailRepository;
import com.enigma.orderin.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    final OrderDetailRepository orderDetailRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderDetailResponse createNewOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) {
            throw new IllegalArgumentException("Product name must not be empty or null");
        }
        orderDetailRepository.createOrderDetail(orderDetail.getOrder().getId(), orderDetail.getProductDetail().getId(), orderDetail.getQuantity());
        Integer lastInsertId = orderDetailRepository.getLastInsertedId();

        if (lastInsertId  == 0) {
            throw new RuntimeException("Failed to create the product");
        }

        return OrderDetailResponse.builder()
                .orderDetailId(lastInsertId.intValue())
                .product(ProductResponse.builder()
                        .productId(orderDetail.getProductDetail().getProduct().getId())
                        .price(orderDetail.getProductDetail().getPrice())
                        .stock(orderDetail.getProductDetail().getStock())
                        .price(orderDetail.getProductDetail().getPrice())
                        .isActive(orderDetail.getProductDetail().getIsActive())
                        .build())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
