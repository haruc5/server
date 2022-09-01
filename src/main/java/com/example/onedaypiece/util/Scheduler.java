package com.example.onedaypiece.util;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challengeDetail.dao.ChallengeDetailRepository;
import com.example.onedaypiece.web.challengeDetail.domain.ChallengeDetail;
import com.example.onedaypiece.web.posting.dao.PostingRepository;
import com.example.onedaypiece.web.posting.domain.Posting;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final ChallengeDetailRepository challengeDetailRepository;
    private final PostingRepository postingRepository;
    private final SchedulerQueryRepository schedulerQueryRepository;
    private static final String SCHEDULE_MODE = System.getProperty("schedule.mode");

    LocalDateTime today;

    @Scheduled(cron = "01 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void notWrittenChallengerKick() {

        // nginx 사용시에 여러 인스턴스 모두에서 update 되는 것을 방지.
        if (isNotScheduleMode()) {
            return;
        }
        // today 호출, 스케줄러 실행시의 시간으로 변경.
        initializeToday();

        // 주말 여부 체크를 위한 week 변수 생성
        int week = today.getDayOfWeek().getValue();

        // 주말 여부를 체크해서 챌린지 레코드를 가져옴.
        List<Integer> challengeId = schedulerQueryRepository.findAllByChallenge(week);
    }

    @Scheduled(cron = "04 0 0 * * *") // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    public void changePostingApproval() {
        if (isNotScheduleMode()) {
            return;
        }
        // 진행중인 챌린지에서 포스팅 작성
        List<Posting> approvalPostingList = schedulerQueryRepository.findChallengeMember();
        // 포스팅의 인증여부 업데이트.
        int postingApprovalUpdate = postingRepository.updatePostingApproval(approvalPostingList);
    }
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
        List<ChallengeDetail> detailList = schedulerQueryRepository.findAllByChallengeProgressLessThan(3L);

        List<Challenge> challengeList = detailList
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
        Long progress2 = schedulerQueryRepository.updateChallengeProgress(2L, startList);
    }

    private void challengeEnd(List<Challenge> endList) {
        Long progress3 = schedulerQueryRepository.updateChallengeProgress(3L, endList);
    }

    private boolean isChallengeTimeToStart(Challenge c) {
        return c.getChallengeProgress() == 1L && c.getChallengeStart().isEqual(ChronoLocalDate.from(today));
    }

    private boolean isChallengeTimeToEnd(Challenge c) {
        return c.getChallengeProgress() == 2L && c.getChallengeEnd().isBefore(ChronoLocalDate.from(today));
    }

    private boolean isNotScheduleMode() {
        return null == SCHEDULE_MODE || !SCHEDULE_MODE.equals("on");
    }

    public void initializeToday() {
        today = LocalDate.now().atStartOfDay();
    }

}
