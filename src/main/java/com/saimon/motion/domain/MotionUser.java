package com.saimon.motion.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

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
                this.roles
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
        private List<Role> roles;
    }
}