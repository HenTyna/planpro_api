package com.planprostructure.planpro.domain.users;

import com.planprostructure.planpro.enums.Role;
import com.planprostructure.planpro.enums.StatusUser;
import jakarta.persistence.*;
import lombok.Builder;
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

    @Column(name = "usr_dob")
    private String dateOfBirth;

    @Column(name = "usr_fn")
    private String firstName;

    @Column(name = "usr_ln")
    private String lastName;

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

    @Column(name = "gender")
    private String gender;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Builder
    public Users(String username, String firstName, String lastName, String dateOfBirth, String password, String email, Role role, String phoneNumber, StatusUser status, LocalDateTime resetTokenExpiry, String resetToken, String gender, String profileImageUrl) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.resetTokenExpiry = resetTokenExpiry;
        this.resetToken = resetToken;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }


}
