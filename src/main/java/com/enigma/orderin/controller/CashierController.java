package com.enigma.orderin.controller;

import com.enigma.orderin.constant.AppPath;
import com.enigma.orderin.dto.response.AdminResponse;
import com.enigma.orderin.dto.response.CashierResponse;
import com.enigma.orderin.dto.response.CommonResponse;
import com.enigma.orderin.entity.Admin;
import com.enigma.orderin.entity.Cashier;
import com.enigma.orderin.service.AdminService;
import com.enigma.orderin.service.CashierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.CASHIER)
public class CashierController {
    private final CashierService cashierService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody Cashier cashier) {
        CashierResponse cashierResponse = cashierService.createCashier(cashier);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<CashierResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new Cashier")
                        .data(cashierResponse).build());
    }
}
