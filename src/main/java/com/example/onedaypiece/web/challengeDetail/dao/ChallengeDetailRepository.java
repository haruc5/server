package com.example.onedaypiece.web.challengeDetail.dao;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challengeDetail.domain.ChallengeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeDetailRepository extends JpaRepository<ChallengeDetail, Integer> {
    ChallengeDetail findByChallengeAndChallengeDetailStatusTrue(Challenge challenge);

    List<ChallengeDetail> findAllByChallengeAndChallengeDetailStatusTrue(Challenge challenge);
}
