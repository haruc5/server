package com.example.onedaypiece.wep.challengeDetail.dao;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeDetailRepository extends JpaRepository<ChallengeDetail, Integer> {
    ChallengeDetail findByChallengeAndChallengeDetailStatusTrue(Challenge challenge);

    List<ChallengeDetail> findAllByChallengeAndChallengeDetailStatusTrue(Challenge challenge);
}
