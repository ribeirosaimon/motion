package com.saimon.motion.api.service;

import com.saimon.motion.DTOs.SignInDTO;
import com.saimon.motion.domain.AdminPromotion;
import com.saimon.motion.domain.MotionUser;
import com.saimon.motion.domain.Profile;
import com.saimon.motion.exception.MotionException;
import com.saimon.motion.repository.AdminPromotionRepository;
import com.saimon.motion.repository.ProfileRepository;
import com.saimon.motion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MotionUserService {

    private final UserRepository userRepository;
    private final AdminPromotionRepository adminPromotionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    public MotionUser.MotionUserRef signUpUser(SignInDTO newUserDTO) {

        if (userRepository.existsByUsername(newUserDTO.getUsername())) {
            throw new MotionException("User already exists");
        }

        MotionUser motionUser = new MotionUser();
        motionUser.setName(newUserDTO.getName());
        motionUser.setUsername(newUserDTO.getUsername());
        motionUser.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        motionUser.setPhone(newUserDTO.getPhone());
        motionUser.setGender(newUserDTO.getGender());
        motionUser.setLoginAttemp(0);
        motionUser.setLoginCount(0L);
        motionUser.setStatus(MotionUser.Status.ACTIVE);
        motionUser.setRole(MotionUser.Role.USER);

        motionUser.setCreatedAt(new Date());
        motionUser.setUpdatedAt(new Date());
        MotionUser userRef = userRepository.save(motionUser);

        this.createUserProfile(userRef);
        return userRef.getUserRef();
    }

    public void inactiveUser(Long loggedUserId, Long bannedUserId) throws Exception {
        MotionUser motionUser = userRepository.findById(loggedUserId)
                .orElseThrow(() -> new MotionException("User not found"));
        //admin can ban USERS
        if (motionUser.getRole().equals(MotionUser.Role.ADMIN)) {
            if (bannedUserId != null) {
                MotionUser bannedUser = userRepository.findById(bannedUserId)
                        .orElseThrow(() -> new MotionException("User not found"));
                bannedUser.setStatus(MotionUser.Status.INACTIVE);
                userRepository.save(bannedUser);

                AdminPromotion adminPromotion = adminPromotionRepository
                        .findByMotionUser(motionUser).orElseThrow(Exception::new);

                Integer countBanUser = adminPromotion.getCountBanUser();
                adminPromotion.setCountPromotionUser(countBanUser + 1);
            } else {
                motionUser.setStatus(MotionUser.Status.INACTIVE);
            }
        } else {
            motionUser.setStatus(MotionUser.Status.INACTIVE);
        }
        userRepository.save(motionUser);
    }

    @Transactional
    public MotionUser.MotionUserRef promoteToAdmin(MotionUser.MotionUserRef loggedUser, Long promotedUserId) throws Exception {
        MotionUser motionUser = userRepository.findById(promotedUserId)
                .orElseThrow(() -> new MotionException("User not found"));

        if (motionUser.getRole().equals(MotionUser.Role.ADMIN)) {
            throw new MotionException("you already Admin");
        }
        AdminPromotion adminPromotion = new AdminPromotion();

        adminPromotion.setPromotedAt(new Date());
        adminPromotion.setMotionUser(motionUser);
        adminPromotion.setCountBanUser(0);
        adminPromotion.setCountPromotionUser(0);
        AdminPromotion loggedAdmin = adminPromotionRepository.findByMotionUserId(loggedUser.getId()).orElseThrow(Exception::new);
        loggedAdmin.setCountPromotionUser(loggedAdmin.getCountPromotionUser() + 1);
        adminPromotionRepository.saveAll(List.of(adminPromotion, loggedAdmin));

        motionUser.setAdminPromotion(adminPromotion);
        motionUser.setRole(MotionUser.Role.ADMIN);
        userRepository.save(motionUser);
        return null;
    }

    private Profile createUserProfile(MotionUser userRef){
        Profile profile = new Profile();
        profile.setMotionUser(userRef);
        profile.setBirthday(userRef.getBirthday());
        profile.setSharedBy(Profile.SharedBy.ME);
        profile.setStatus(Profile.Status.ACTIVE);
        profile.setName(userRef.getName());
        return profileRepository.save(profile);
    }
}
