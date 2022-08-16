package com.example.onedaypiece.wep.challenge.service;

import com.example.onedaypiece.wep.challenge.dao.ChallengeQueryRepository;
import com.example.onedaypiece.wep.challenge.dao.ChallengeRepository;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.exception.DataNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    // 챌린지 전체 조회
    public List<Challenge> getChallenges() {
        return challengeRepository.findAll();
    }

    // 챌린지 단건조회(조회수 포함)
    public Challenge getChallenge(Integer challengeId) {
        Optional<Challenge> ViewChallenge = this.challengeQueryRepository.findById(challengeId);
        if (ViewChallenge.isPresent()) {
            Challenge challenge = ViewChallenge.get();
            challenge.setViewCount(challenge.getViewCount() + 1);
            this.challengeRepository.save(challenge);
            return challenge;
        } else {
            throw new DataNotFoundException("challenge not found");
        }
    }

    // 챌린지 생성
    @Transactional
    public void postChallenge(Challenge challenge) {
        Challenge postChallenge = Challenge.createChallenge(challenge);
        challengeRepository.save(postChallenge);
    }

    @Transactional
    public void deleteChallenge(Integer challengeId) {

    }

}
