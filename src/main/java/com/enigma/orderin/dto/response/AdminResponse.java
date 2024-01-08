package com.enigma.orderin.dto.response;


import com.enigma.orderin.entity.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdminResponse {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String userCredentialId;
}
