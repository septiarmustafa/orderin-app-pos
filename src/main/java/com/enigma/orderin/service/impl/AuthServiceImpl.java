package com.enigma.orderin.service.impl;

import com.enigma.orderin.constant.ERole;
import com.enigma.orderin.dto.request.AuthRequest;
import com.enigma.orderin.dto.response.LoginResponse;
import com.enigma.orderin.dto.response.RegisterResponse;
import com.enigma.orderin.entity.*;
import com.enigma.orderin.repository.UserCredentialRepository;
import com.enigma.orderin.security.JwtUtil;
import com.enigma.orderin.service.AdminService;
import com.enigma.orderin.service.AuthService;
import com.enigma.orderin.service.CashierService;
import com.enigma.orderin.service.RoleService;
import com.enigma.orderin.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CashierService cashierService;
    private final AdminService adminService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerCashier(AuthRequest authRequest) {
        try {
            validationUtil.validate(authRequest);
            Role role = Role.builder()
                    .name(ERole.ROLE_CASHIER)
                    .build();
            role = roleService.getOrSave(role);

            UserCredential userCredential = UserCredential.builder()
                    .email(authRequest.getEmail().toLowerCase())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            Cashier cashier = Cashier.builder()
                    .userCredential(userCredential)
                    .name(authRequest.getName())
                    .phoneNumber(authRequest.getPhoneNumber())
                    .build();
            cashierService.createCashier(cashier);

            return RegisterResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getName().toString())
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest authRequest) {
        try {
            validationUtil.validate(authRequest);
            Role role = Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build();
            role = roleService.getOrSave(role);

            UserCredential userCredential = UserCredential.builder()
                    .email(authRequest.getEmail().toLowerCase())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            Admin admin = Admin.builder()
                    .userCredential(userCredential)
                    .name(authRequest.getName())
                    .phoneNumber(authRequest.getPhoneNumber())
                    .build();
            adminService.createAdmin(admin);

            return RegisterResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getName().toString())
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        validationUtil.validate(authRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail().toLowerCase().toLowerCase(),
                authRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);
        return LoginResponse.builder()
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }
}
