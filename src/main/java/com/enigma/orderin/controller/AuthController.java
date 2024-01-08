package com.enigma.orderin.controller;

import com.enigma.orderin.constant.AppPath;
import com.enigma.orderin.dto.request.AuthRequest;
import com.enigma.orderin.dto.response.CommonAuthResponse;
import com.enigma.orderin.dto.response.LoginResponse;
import com.enigma.orderin.dto.response.RegisterResponse;
import com.enigma.orderin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API+AppPath.AUTH)
public class AuthController {
    private final AuthService authService;

    @PostMapping(AppPath.ADMIN + AppPath.REGISTER)
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerAdmin(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonAuthResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully register")
                        .data(registerResponse).build());
    }
    @PostMapping(AppPath.CASHIER + AppPath.REGISTER)
    public ResponseEntity<?> registerCashier(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerCashier(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonAuthResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully register")
                        .data(registerResponse).build());
    }

    @PostMapping(AppPath.LOGIN)
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        LoginResponse loginResponse = authService.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonAuthResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully login")
                        .data(loginResponse).build());
    }
}
