package com.planprostructure.planpro.domain.users;

import com.planprostructure.planpro.enums.Role;
import com.planprostructure.planpro.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming the ID is auto-generated
    private Long id;

    @Column(name = "usr_nm")
    private String username;

    @Column(name = "pwd")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role; // e.g., ADMIN, USER, etc.

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "sts",nullable = false, length = Types.CHAR)
    @JdbcTypeCode(Types.CHAR)
    @Convert(converter = StatusUser.Converter.class)
    private StatusUser status;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

}
