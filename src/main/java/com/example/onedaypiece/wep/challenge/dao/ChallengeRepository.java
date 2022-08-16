package com.example.onedaypiece.wep.challenge.dao;


import com.example.onedaypiece.wep.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    Optional<Challenge> findByChallengeStatusTrueAndChallengeId(Integer challengeId);
}
