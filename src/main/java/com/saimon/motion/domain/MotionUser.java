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
    private String phone;
    private Long loginCount;
    private Integer loginAttemp;
    private Date lastLoginAttemp;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date createdAt;
    private Date updatedAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_promotion_id")
    private AdminPromotion adminPromotion;

    public enum Gender {
        LESBIAN,
        GAY,
        HETERO,
        TRANS
    }

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
                this.gender,
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
        private Gender gender;
        private String phone;
        private Role role;
    }
}