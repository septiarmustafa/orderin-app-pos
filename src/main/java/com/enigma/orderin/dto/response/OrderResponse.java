package com.enigma.orderin.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderResponse {
    private Integer orderId;
    private LocalDate transactionDate;
    private CashierResponse cashier;
    private List<OrderDetailResponse> listOrderDetail;
}
