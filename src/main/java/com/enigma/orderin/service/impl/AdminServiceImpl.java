package com.enigma.orderin.service.impl;

import com.enigma.orderin.dto.response.AdminResponse;
import com.enigma.orderin.dto.response.CashierResponse;
import com.enigma.orderin.entity.Admin;
import com.enigma.orderin.entity.Cashier;
import com.enigma.orderin.repository.AdminRepository;
import com.enigma.orderin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminResponse createAdmin(Admin admin) {
        Admin adminSave = adminRepository.saveAndFlush(admin);
        return AdminResponse.builder()
                .id(adminSave.getId())
                .name(adminSave.getName())
                .phoneNumber(adminSave.getPhoneNumber())
                .build();
    }

    @Override
    public AdminResponse getById(Integer id) {
        Admin admin = adminRepository.findById(id).orElse(null);

        if (admin == null) {
            return null;
        }
        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .phoneNumber(admin.getPhoneNumber())
                .build();
    }

    @Override
    public List<AdminResponse> getAllAdmin() {
        List<Admin> listAdmin = adminRepository.findAll();
        return listAdmin.stream().map(
                        admin -> AdminResponse.builder()
                                .id(admin.getId())
                                .name(admin.getName())
                                .phoneNumber(admin.getPhoneNumber())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponse updateAdmin(Admin admin) {
        Admin admins = adminRepository.findById(admin.getId()).orElse(null);
        if (admins != null) {
            admins = Admin.builder()
                    .id(admin.getId())
                    .name(admin.getName())
                    .phoneNumber(admin.getPhoneNumber())
                    .build();
            adminRepository.save(admins);
            return AdminResponse.builder()
                    .id(admins.getId())
                    .name(admins.getName())
                    .phoneNumber(admins.getPhoneNumber())
                    .build();
        }
        return null;
    }
    @Override
    public void deleteAdmin(Integer id) {
        adminRepository.deleteById(id);
    }
}
