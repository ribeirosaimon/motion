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
    @JoinColumn(name = "motionUser_id")
    private MotionUser motionUser;
    private Integer countBanUser;
    private Integer countPromotionUser;
}
