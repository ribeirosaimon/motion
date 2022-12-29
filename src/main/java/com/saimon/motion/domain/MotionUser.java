package com.saimon.motion.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Entity
public class MotionUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String cpf;
    private String phone;
    private Long loginCount;
    private Integer loginAttemp;
    private Date lastLoginAttemp;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Date createdAt;
    private Date updatedAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adminPromotionId", referencedColumnName = "id")
    private AdminPromotion promotedAt;

    public enum Role {
        ADMIN,
        USER
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

    public MotionUserRef getUserRef() {
        return new MotionUserRef(
                this.id,
                this.name,
                this.username,
                this.cpf,
                this.phone,
                this.role
        );
    }

    @Data
    @AllArgsConstructor
    public static class MotionUserRef {
        private Long id;
        private String name;
        private String username;
        private String cpf;
        private String phone;
        private Role role;
    }
}