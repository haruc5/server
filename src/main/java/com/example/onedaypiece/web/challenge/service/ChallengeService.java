package com.example.onedaypiece.web.challenge.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.exception.DataNotFoundException;
import com.example.onedaypiece.web.challenge.dao.ChallengeQueryRepository;
import com.example.onedaypiece.web.challenge.dao.ChallengeRepository;
import com.example.onedaypiece.web.challenge.domain.*;
import com.example.onedaypiece.web.challengeDetail.dao.ChallengeDetailQueryRepository;
import com.example.onedaypiece.web.challengeDetail.dao.ChallengeDetailRepository;
import com.example.onedaypiece.web.challengeDetail.domain.ChallengeDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.onedaypiece.web.challenge.domain.ChallengeCategory.*;
import static com.example.onedaypiece.web.challenge.domain.ChallengeMainDto.createChallengeMainDto;
import static com.example.onedaypiece.web.challenge.domain.ChallengeSourceDto.createChallengeSourceDto;
import static com.example.onedaypiece.web.challengeDetail.domain.ChallengeDetail.createChallengeDetail;

@RequiredArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeDetailRepository challengeDetailRepository;
    private final ChallengeDetailQueryRepository challengeDetailQueryRepository;

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

    public List<Challenge> getChallengeList() {
        return this.challengeRepository.findAll();
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

        List<ChallengeDetail> detailList = challengeDetailRepository.findAllByChallengeAndChallengeDetailStatusTrue(challenge);
        detailList.forEach(ChallengeDetail::setStatusFalse);
        challenge.updateChallengeProgress(3L);
        challenge.setChallengeStatusFalse();
    }

    public void deleteChallengeAll() {
        challengeQueryRepository.deleteAllStatusFalse();
    }

    private Challenge challengeChecker(Integer challengeId) {
        return challengeQueryRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다"));
    }

    //메인 페이지
    public ChallengeMainDto getMainPage() {
        List<ChallengeDetail> details = challengeDetailQueryRepository.findAllByStatusTrue();

        return createChallengeMainDto(
                popularCollector(details),
                categoryCollector(EXERCISE, details),
                categoryCollector(LIVINGHABITS, details),
                categoryCollector(NODRINK, details),
                categoryCollector(NOSMOKE, details));
    }

    private List<ChallengeSourceDto> popularCollector(List<ChallengeDetail> details) {
        final int POPULAR_SIZE = 4;

        List<ChallengeDetail> popularDetails = challengeDetailQueryRepository
                .findAllPopular(PageRequest.of(0, POPULAR_SIZE));
        return popularDetails
                .stream()
                .map(r -> (createChallengeSourceDto(r.getChallenge(), details)))
                .collect(Collectors.toList());
    }

    private List<ChallengeSourceDto> categoryCollector(ChallengeCategory category, List<ChallengeDetail> details) {
        final int CATEGORY_SIZE = 3;

        List<Challenge> challenges = details
                .stream()
                .map(ChallengeDetail::getChallenge)
                .filter(challenge -> challenge.getChallengeCategory().equals(category))
                .distinct()
                .limit(CATEGORY_SIZE)
                .collect(Collectors.toList());

        return challenges
                .stream()
                .map(c -> createChallengeSourceDto(c, details))
                .collect(Collectors.toList());
    }

}
