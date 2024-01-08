package com.enigma.orderin.controller;

import com.enigma.orderin.constant.AppPath;
import com.enigma.orderin.dto.request.OrderRequest;
import com.enigma.orderin.dto.response.CommonResponse;
import com.enigma.orderin.dto.response.OrderResponse;
import com.enigma.orderin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.ORDER)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createNewOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new order")
                        .data(orderResponse).build());
    }

}
