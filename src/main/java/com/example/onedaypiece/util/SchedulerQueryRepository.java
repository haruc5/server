package com.example.onedaypiece.util;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challengeDetail.domain.ChallengeDetail;
import com.example.onedaypiece.web.challengeDetail.domain.QChallengeDetail;
import com.example.onedaypiece.web.posting.domain.Posting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.onedaypiece.web.challenge.domain.QChallenge.challenge;
import static com.example.onedaypiece.web.challengeDetail.domain.QChallengeDetail.challengeDetail;
import static com.example.onedaypiece.web.posting.domain.QPosting.posting;

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
                        challengeDetail.challenge.challengeProgress.eq(2L))
                .fetch();
    }

    //챌린지 진행 상태
    @Modifying
    public Long updateChallengeProgress(Long progress, List<Challenge> challengeList) {
        return queryFactory.update(challenge)
                .set(challenge.challengeProgress, progress)
                .where(challenge.in(challengeList))
                .execute();
    }

    //진행 상태 업데이트 목록
    public List<ChallengeDetail> findAllByChallengeProgressLessThan(Long progress) {
        return queryFactory
                .selectFrom(challengeDetail)
                .innerJoin(challengeDetail.challenge).fetchJoin()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.lt(progress))
                .fetch();
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
                        challenge.challengeProgress.eq(1L),
                        challenge.challengeTitle.eq(title))
                .fetchFirst() == null;
    }

    //포스팅 개수
    public Long findAllByChallenge(Challenge challenge) {
        return queryFactory.select(posting.challenge.challengeId)
                .from(posting)
                .where(posting.challenge.challengeStatus.isTrue(),
                        posting.challenge.eq(challenge))
                .fetchCount();
    }

    //포스팅 등록 인원 확인용 포스팅
    public List<Posting> findChallengeMember() {
        QChallengeDetail challengeDetailSub = new QChallengeDetail("challengeDetailSub");

        return queryFactory.select(posting)
                .from(posting)
                .join(challengeDetail).on(challengeDetail.challenge.eq(posting.challenge))
                .join(posting.challenge,challenge)
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.eq(2L),
                        posting.postingApproval.isFalse(),
                        posting.postingCount.goe(
                                JPAExpressions.select(challengeDetailSub.challengeDetailId.count())
                                        .from(challengeDetailSub)
                                        .where(challengeDetailSub.challenge.challengeId.eq(challengeDetail.challenge.challengeId),
                                                challengeDetailSub.challengeDetailStatus.isTrue())
                        ))
                .fetch();
    }

}
