package com.example.onedaypiece.wep.challengeDetail.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.wep.challenge.dao.ChallengeQueryRepository;
import com.example.onedaypiece.wep.challenge.dao.ChallengeRepository;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challenge.domain.ChallengeDto;
import com.example.onedaypiece.wep.challenge.domain.ChallengeListDto;
import com.example.onedaypiece.wep.challengeDetail.dao.ChallengeDetailQueryRepository;
import com.example.onedaypiece.wep.challengeDetail.dao.ChallengeDetailRepository;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.onedaypiece.wep.challenge.domain.ChallengeDto.createChallengeDto;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class ChallengeDetailService {
    private final ChallengeDetailRepository challengeDetailRepository;
    private final ChallengeDetailQueryRepository challengeDetailQueryRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    final int SEARCH_SIZE = 8;

    @Transactional
    public void requestChallenge(ChallengeDetailDto challengeDetailDto) {

        Challenge challenge = ChallengeChecker(challengeDetailDto.getChallengeId());

        ChallengeDetail detail = ChallengeDetail.createChallengeDetail(challenge);
        challengeDetailRepository.save(detail);
    }

    @Transactional
    public void giveUpChallenge(Integer challengeId) {
        Challenge challenge = ChallengeChecker(challengeId);

        ChallengeDetail detail = challengeDetailRepository
                .findByChallengeAndChallengeDetailStatusTrue(challenge);
        detail.setStatusFalse();
    }

    private Challenge ChallengeChecker(Integer challengeId){
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiRequestException("존재하지 않는 챌린지입니다."));
    }

    public ChallengeListDto getChallengeBySearch(String word, String categoryName,
                                                 int period, int progress, int page) {
        Pageable pageable = PageRequest.of(page - 1, SEARCH_SIZE);
        Slice<Challenge> challengeList = challengeQueryRepository
                .findAllBySearch(word, categoryName, period, progress, pageable);

        Map<Challenge, List<ChallengeDetail>> detailMap = challengeDetailQueryRepository
                .findAllByChallengeList(challengeList).stream()
                .collect(Collectors.groupingBy(ChallengeDetail::getChallenge));

        return getChallengeListDto(challengeList, detailMap);
    }

    public ChallengeListDto getChallengeSearchResult(String searchWords, int page){
        Slice<Challenge> challengeList = challengeQueryRepository
                .findAllByWord(searchWords.trim(), PageRequest.of(page - 1, SEARCH_SIZE));

        Map<Challenge, List<ChallengeDetail>> detailMap = challengeDetailQueryRepository
                .findAllByChallengeList(challengeList)
                .stream()
                .collect(Collectors.groupingBy(ChallengeDetail::getChallenge));

        return getChallengeListDto(challengeList, detailMap);
    }

    private ChallengeListDto getChallengeListDto(Slice<Challenge> challengeList,
                                                 Map<Challenge, List<ChallengeDetail>> detailMap) {
        List<ChallengeDto> challengeDtoList = challengeList
                .stream()
                .filter(c -> !isEmpty(detailMap.get(c)))
                .map(c -> createChallengeDto(c, challengeList.getNumber()))
                .collect(Collectors.toList());

        return ChallengeListDto.createChallengeListDto(challengeDtoList, challengeList.hasNext());
    }
}
