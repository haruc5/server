package com.example.onedaypiece.wep.challenge.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.exception.DataNotFoundException;
import com.example.onedaypiece.wep.challenge.dao.ChallengeQueryRepository;
import com.example.onedaypiece.wep.challenge.dao.ChallengeRepository;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challenge.domain.UpdateChallengeDto;
import com.example.onedaypiece.wep.challengeDetail.dao.ChallengeDetailQueryRepository;
import com.example.onedaypiece.wep.challengeDetail.dao.ChallengeDetailRepository;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail.createChallengeDetail;

@RequiredArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeDetailRepository challengeDetailRepository;
    private final ChallengeDetailQueryRepository challengeDetailQueryRepository;

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

        Integer challengeId = challenge.getChallengeId();
        ChallengeDetail postChallengeDetail = createChallengeDetail(postChallenge);
        challengeDetailRepository.save(postChallengeDetail);
    }

    //챌린지 수정
    @Transactional
    public void updateChallenge(UpdateChallengeDto updateChallengeDto) {
        Challenge challenge = challengeChecker(updateChallengeDto.getChallengeId());
        challenge.updateChallenge(updateChallengeDto);
    }

    // 챌린지 삭제
    @Transactional
    public void deleteChallenge(Integer challengeId) {
        Challenge challenge = challengeChecker(challengeId);
//      deleteChallengeException(challenge);

        List<ChallengeDetail> detailList = challengeDetailRepository.findAllByChallengeAndChallengeDetailStatusTrue(challenge);
        detailList.forEach(ChallengeDetail::setStatusFalse);
    }

    private Challenge challengeChecker(Integer challengeId) {
        return challengeQueryRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다"));
    }

//    private void deleteChallengeException(Challenge challenge) {
//
//        if (LocalDate.now().isBefore(challenge.getChallengeStart())) {
//            challenge.setChallengeStatusFalse();
//            challenge.updateChallengeProgress(3);
//        } else {
//            throw new ApiRequestException("이미 시작된 챌린지는 삭제할 수 없습니다.");
//        }
//    }
}
