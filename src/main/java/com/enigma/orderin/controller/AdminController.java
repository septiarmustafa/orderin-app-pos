package com.enigma.orderin.controller;

import com.enigma.orderin.constant.AppPath;
import com.enigma.orderin.dto.response.AdminResponse;
import com.enigma.orderin.dto.response.CommonResponse;
import com.enigma.orderin.entity.Admin;
import com.enigma.orderin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.ADMIN)
public class AdminController {
        private final AdminService adminService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        AdminResponse adminResponse = adminService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<AdminResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new Admin")
                        .data(adminResponse).build());
    }

}
