package com.saimon.motion.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String familyName;
    private Integer age;
    private Date birthday;
    @Enumerated(EnumType.STRING)
    private SharedBy sharedBy;
    @Enumerated(EnumType.STRING)
    private RelationShip relationShip;
    @OneToOne
    @JoinColumn(name = "motion_user_id")
    private MotionUser motionUser;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date createdAt;
    private Date updatedAt;
    public enum RelationShip {
        SINGLE,
        MARRIED
    }
    public enum SharedBy {
        ME,
        FRIENDS,
        ALL
    }
    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
