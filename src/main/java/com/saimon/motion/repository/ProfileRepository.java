package com.saimon.motion.repository;

import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.domain.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Optional<Profile> findProfileByMotionUser_Id(Long id);
}
