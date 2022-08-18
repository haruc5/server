package com.example.onedaypiece.util;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.wep.challenge.domain.QChallenge.challenge;
import static com.example.onedaypiece.wep.challengeDetail.domain.QChallengeDetail.challengeDetail;
import static com.example.onedaypiece.wep.posting.domain.QPosting.posting;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class SchedulerQueryRepository {

    private final JPAQueryFactory queryFactory;

    //주말 여부
    private BooleanExpression isHoliday(int week){
        BooleanExpression notEmpty = challengeDetail.challenge.challengeHoliday.isNotEmpty();
        return week == 6 || week == 7 ? notEmpty : null;
    }

    //진행중인 챌린지
    public List<Integer> findAllByChallenge(int week){

        return queryFactory.select(challengeDetail.challenge.challengeId)
                .distinct()
                .from(challengeDetail)
                .join(challengeDetail.challenge,challenge)
                .where(isHoliday(week),
                        challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.eq(2))
                .fetch();
    }

    //진행 상태 업데이트 목록
    public List<ChallengeDetail> findAllByChallengeProgressLessThan(Integer progress) {
        return queryFactory
                .selectFrom(challengeDetail)
                .innerJoin(challengeDetail.challenge).fetchJoin()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.lt(progress))
                .fetch();
    }

    //챌린지 진행 상태
    @Modifying
    public Long updateChallengeProgress(Integer progress, List<Challenge> challengeList) {
        return queryFactory.update(challenge)
                .set(challenge.challengeProgress, progress)
                .where(challenge.in(challengeList))
                .execute();
    }

    //수정 가능 여부(당일 한정)
    public List<Integer> findSchedulerUpdatePostingModifyOk(LocalDateTime today) {
        return queryFactory.select(posting.postingId)
                .from(posting)
                .where(posting.postingStatus.isTrue(),
                        posting.postingModifyOk.isTrue(),
                        posting.createdAt.lt(today))
                .fetch();
    }

    //공식 챌린지
    public Boolean findAllByOfficialAndChallengeTitle(String title) {
        return queryFactory.select(challenge.challengeId)
                .from(challenge)
                .where(challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(1),
                        challenge.challengeTitle.eq(title))
                .fetchFirst() == null;
    }
}
