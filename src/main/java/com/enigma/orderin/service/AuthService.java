package com.enigma.orderin.service;

import com.enigma.orderin.dto.request.AuthRequest;
import com.enigma.orderin.dto.response.LoginResponse;
import com.enigma.orderin.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCashier (AuthRequest authRequest);
    RegisterResponse registerAdmin (AuthRequest authRequest);
    LoginResponse login(AuthRequest authRequest);
}
