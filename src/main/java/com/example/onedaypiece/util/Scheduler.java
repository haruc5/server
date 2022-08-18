package com.example.onedaypiece.util;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challengeDetail.dao.ChallengeDetailRepository;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import com.example.onedaypiece.wep.posting.dao.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final PostingRepository postingRepository;
    private final ChallengeDetailRepository challengeDetailRepository;
    private final SchedulerQueryRepository schedulerQueryRepository;

    private static final String SCHEDULE_MODE = System.getProperty("schedule.mode");

    LocalDateTime today;

    @Scheduled(cron = "05 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void updatePostingModifyOk() {

        if (isNotScheduleMode()) {
            return;
        }
        // 작성 당일이 지나면 수정 불가능하게 만드는 쿼리
        List<Integer> postingIdList = schedulerQueryRepository.findSchedulerUpdatePostingModifyOk(today);
        // 벌크성 쿼리 업데이트
        Integer updateResult = postingRepository.updatePostingModifyOk(postingIdList);
    }

    @Scheduled(cron = "07 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void challengeStatusUpdate() {

        if (isNotScheduleMode()) {
            return;
        }
        List<ChallengeDetail> recordList = schedulerQueryRepository.findAllByChallengeProgressLessThan(3);

        List<Challenge> challengeList = recordList
                .stream()
                .map(ChallengeDetail::getChallenge)
                .distinct()
                .collect(Collectors.toList());

        List<Challenge> startList = challengeList
                .stream()
                .filter(this::isChallengeTimeToStart)
                .collect(Collectors.toList());

        List<Challenge> endList = challengeList
                .stream()
                .filter(this::isChallengeTimeToEnd)
                .collect(Collectors.toList());

        // 챌린지 시작
        challengeStart(startList);

        // 챌린지 종료
        challengeEnd(endList);
    }

    private void challengeStart(List<Challenge> startList) {
        Long result1 = schedulerQueryRepository.updateChallengeProgress(2, startList);
    }

    private void challengeEnd(List<Challenge> endList) {
        Long result2 = schedulerQueryRepository.updateChallengeProgress(3, endList);
    }

    private boolean isChallengeTimeToStart(Challenge c) {
        return c.getChallengeProgress() == 1 && c.getChallengeStart().isEqual(ChronoLocalDate.from(today));
    }

    private boolean isChallengeTimeToEnd(Challenge c) {
        return c.getChallengeProgress() == 2 && c.getChallengeEnd().isBefore(ChronoLocalDate.from(today));
    }

    private boolean isNotScheduleMode() {
        return null == SCHEDULE_MODE || !SCHEDULE_MODE.equals("on");
    }
}
