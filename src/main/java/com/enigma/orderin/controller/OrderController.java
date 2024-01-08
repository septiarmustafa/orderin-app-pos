package com.enigma.orderin.controller;

import com.enigma.orderin.constant.AppPath;
import com.enigma.orderin.dto.request.OrderRequest;
import com.enigma.orderin.dto.response.CashierResponse;
import com.enigma.orderin.dto.response.CommonResponse;
import com.enigma.orderin.dto.response.OrderResponse;
import com.enigma.orderin.dto.response.ProductResponse;
import com.enigma.orderin.entity.Cashier;
import com.enigma.orderin.entity.Order;
import com.enigma.orderin.entity.Product;
import com.enigma.orderin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.ORDER)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createNewOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new order")
                        .data(orderResponse).build());
    }

    @GetMapping(value = AppPath.ID_ORDER)
    public ResponseEntity<?> getById (@PathVariable(name = "id_order") Integer id){
        OrderResponse orderResponse = orderService.getOrderResponseById(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully get by id order")
                        .data(orderResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllOrder (){
        List<OrderResponse> orderResponse = orderService.getAllOrder();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully get all order")
                        .data(orderResponse).build());
    }

}
