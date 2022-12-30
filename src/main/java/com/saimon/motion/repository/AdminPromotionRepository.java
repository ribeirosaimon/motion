package com.saimon.motion.repository;

import com.saimon.motion.domain.AdminPromotion;
import com.saimon.motion.domain.MotionUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminPromotionRepository extends CrudRepository<AdminPromotion, Long> {
    Optional<AdminPromotion> findByMotionUser(MotionUser motionUser);
    Optional<AdminPromotion> findByMotionUserId(Long motionUserId);
}
