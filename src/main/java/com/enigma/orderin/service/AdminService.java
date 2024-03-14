package com.enigma.orderin.service;

import com.enigma.orderin.entity.Admin;
import com.enigma.orderin.dto.response.AdminResponse;

import java.util.List;

public interface AdminService {
    AdminResponse createAdmin (Admin admin);

    AdminResponse getById (Integer id);

    List<AdminResponse> getAllAdmin();

    AdminResponse updateAdmin (Admin admin);
    void deleteAdmin (Integer id);
}
