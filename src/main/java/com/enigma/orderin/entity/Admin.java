package com.enigma.orderin.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "m_admin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_number",unique = true, nullable = false, length = 20)
    private String phoneNumber;

    @OneToOne
    @JoinColumn (name = "user_credential_id")
    private UserCredential userCredential;
}
