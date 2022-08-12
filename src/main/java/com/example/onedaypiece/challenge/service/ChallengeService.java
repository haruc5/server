package com.example.onedaypiece.challenge.service;

import com.example.onedaypiece.challenge.dao.ChallengeQueryRepository;
import com.example.onedaypiece.challenge.dao.ChallengeRepository;
import com.example.onedaypiece.challenge.domain.challenge.Challenge;
import com.example.onedaypiece.exception.DataNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.onedaypiece.challenge.domain.challenge.Challenge.createChallenge;

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
    public Integer postChallenge(Challenge challenge) {
        Challenge postChallenge = createChallenge(challenge);
        challengeRepository.save(challenge);

        Integer challengeId = postChallenge.getChallengeId();
        return challengeId;
    }

}
