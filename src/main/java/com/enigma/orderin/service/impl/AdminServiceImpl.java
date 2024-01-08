package com.enigma.orderin.service.impl;

import com.enigma.orderin.dto.response.AdminResponse;
import com.enigma.orderin.entity.Admin;
import com.enigma.orderin.repository.AdminRepository;
import com.enigma.orderin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

//    Connection conn;
//
//    public AdminServiceImpl() {
//        this.conn = DbConnector.startConnection();
//    }

    @Override
    public AdminResponse createAdmin(Admin admin) {
        Admin adminSave = adminRepository.saveAndFlush(admin);
//        return AdminResponse.builder()
//                .id(adminSave.getId())
//                .name(adminSave.getName())
//                .phoneNumber(adminSave.getPhoneNumber())
//                .build();
//        try{
//            PreparedStatement pr = conn.prepareStatement("insert into m_admin (name, phone_number, user_credential_id) VALUES (?,?,?)");
//            pr.setString(1,admin.getName());
//            pr.setString(2, admin.getPhoneNumber());
//            pr.setString(3, admin.getUserCredential().getId());
//
//            pr.executeUpdate();
//            System.out.println("success add new admin : name= " + admin.getName() + ", phone number= " + admin.getPhoneNumber() + ", user credential id= " + admin.getUserCredential().getId());
//            pr.close();
//        } catch (SQLException e) {
//            System.out.println("failed save data : "+e.getMessage());
//        }
        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .phoneNumber(admin.getPhoneNumber())
                .userCredentialId(admin.getUserCredential().getId())
                .build();
    }

    @Override
    public AdminResponse getById(Integer id) {
        return null;
    }

    @Override
    public List<AdminResponse> getAllAdmin() {
        return null;
    }

    @Override
    public AdminResponse updateAdmin(Admin admin) {
        return null;
    }
    @Override
    public void deleteAdmin(String id) {

    }
}
