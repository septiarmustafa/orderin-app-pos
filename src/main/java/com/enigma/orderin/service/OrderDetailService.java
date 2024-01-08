package com.enigma.orderin.service;

import com.enigma.orderin.dto.response.OrderDetailResponse;
import com.enigma.orderin.entity.OrderDetail;

public interface OrderDetailService {
    OrderDetailResponse createNewOrderDetail(OrderDetail orderDetail);

}
