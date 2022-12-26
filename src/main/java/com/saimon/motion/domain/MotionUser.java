package com.saimon.motion.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    public MotionUserRef getUserRef(){
        return new MotionUserRef(
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
        private String name;
        private String username;
        private String cpf;
        private String phone;

        private List<Role> roles;
    }
}