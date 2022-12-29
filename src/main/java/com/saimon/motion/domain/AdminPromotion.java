package com.saimon.motion.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class AdminPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date promotedAt;
    @OneToOne
    private MotionUser motionUser;
}
