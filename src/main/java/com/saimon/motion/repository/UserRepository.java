package com.saimon.motion.repository;

import com.saimon.motion.domain.MotionUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<MotionUser, Long> {
    Optional<MotionUser> findByUsername(String username);

    Boolean existsByUsername(String username);
}
