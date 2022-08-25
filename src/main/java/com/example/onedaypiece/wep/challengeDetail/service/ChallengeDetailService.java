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
import com.example.onedaypiece.wep.proceed.end.ChallengeEndDto;
import com.example.onedaypiece.wep.proceed.end.EndDto;
import com.example.onedaypiece.wep.proceed.proceed.ChallengeProceedDto;
import com.example.onedaypiece.wep.proceed.proceed.ProceedDto;
import com.example.onedaypiece.wep.proceed.scheduled.ChallengeScheduledDto;
import com.example.onedaypiece.wep.proceed.scheduled.ScheduledDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.onedaypiece.wep.challenge.domain.ChallengeDto.createChallengeDto;
import static com.example.onedaypiece.wep.proceed.end.ChallengeEndDto.createEndDto;
import static com.example.onedaypiece.wep.proceed.proceed.ChallengeProceedDto.createProceedDto;
import static com.example.onedaypiece.wep.proceed.scheduled.ChallengeScheduledDto.createScheduledDto;

@Service
@RequiredArgsConstructor
public class ChallengeDetailService {
    private final ChallengeDetailRepository challengeDetailRepository;
    private final ChallengeDetailQueryRepository challengeDetailQueryRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    final int SEARCH_SIZE = 12;

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
                                                 int period, int progress) {
        //Pageable pageable = PageRequest.of(page - 1, SEARCH_SIZE);
        List<Challenge> challengeList = challengeQueryRepository
                .findAllBySearch(word, categoryName, period, progress);

        Map<Challenge, List<ChallengeDetail>> detailMap = challengeDetailQueryRepository
                .findAllByChallengeList(challengeList).stream()
                .collect(Collectors.groupingBy(ChallengeDetail::getChallenge));

        return getChallengeListDto(challengeList);
    }

    public ChallengeListDto getChallengeSearchResult(String searchWords){
        List<Challenge> challengeList = challengeQueryRepository
                .findAllByWord(searchWords.trim());

        Map<Challenge, List<ChallengeDetail>> detailMap = challengeDetailQueryRepository
                .findAllByChallengeList(challengeList)
                .stream()
                .collect(Collectors.groupingBy(ChallengeDetail::getChallenge));

        return getChallengeListDto(challengeList);
    }

    private ChallengeListDto getChallengeListDto(List<Challenge> challengeList) {
        List<ChallengeDto> challengeDtoList = challengeList
                .stream()
                .map(c -> createChallengeDto(c, challengeList.size()))
                .collect(Collectors.toList());

        return ChallengeListDto.createChallengeListDto(challengeDtoList);
    }

    // 진행중인 챌린지
    public ChallengeProceedDto getProceed(){
        //본인이 참여한 챌린지 기록리스트  1: 진행 예정, 2: 진행 중, 3 : 진행 완료
        List<ChallengeDetail> targetList = challengeDetailQueryRepository.findAllByProgress(2);

        // 본인이 참여한 챌린지 기록리스트 -> 챌린지 가져옴
        List<Challenge> proceeding = targetList.stream()
                .map(challengeDetail -> challengeDetail.getChallenge()).collect(Collectors.toList());


        // 본인이 참여한 챌린지 리스트 -> 가공
        List<ChallengeDetail> challengeDetailList= challengeDetailQueryRepository.findAllByChallenge(proceeding);
        List<ProceedDto> proceedingResult = proceeding.stream()
                .map(challenge-> new ProceedDto(challenge))
                .collect(Collectors.toList());

        return createProceedDto(proceedingResult);
    }

    // 예정인 챌린지
    public ChallengeScheduledDto getScheduled(){
        //본인이 참여한 챌린지 리스트  1: 진행 예정, 2: 진행 중, 3 : 진행 완료
        List<ChallengeDetail> targetList = challengeDetailQueryRepository.findAllByProgress(1);

        List<Challenge> scheduled = targetList.stream()
                .map(challengeDetail -> challengeDetail.getChallenge()).collect(Collectors.toList());

        List<ChallengeDetail> challengeDetailList = challengeDetailQueryRepository.findAllByChallenge(scheduled);
        List<ScheduledDto> scheduledList = scheduled.stream()
                .map(challenge -> new ScheduledDto(challenge))
                .collect(Collectors.toList());

        return createScheduledDto(scheduledList);
    }

    // 종료된 챌린지
    public ChallengeEndDto getEnd(){
        //본인이 참여한 챌린지 리스트  1: 진행 예정, 2: 진행 중, 3 : 진행 완료
        List<ChallengeDetail> targetList = challengeDetailQueryRepository.findAllByProgress(3);

        List<Challenge> end = targetList.stream()
                .map(challengeDetail -> challengeDetail.getChallenge()).collect(Collectors.toList());

        List<ChallengeDetail> challengeDetailList = challengeDetailQueryRepository.findAllByChallenge(end);
        List<EndDto> endList = end.stream()
                .map(challenge -> new EndDto(challenge))
                .collect(Collectors.toList());

        return createEndDto(endList);
    }
}
