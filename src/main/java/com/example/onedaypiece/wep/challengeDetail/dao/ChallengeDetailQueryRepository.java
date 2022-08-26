package com.example.onedaypiece.wep.challengeDetail.dao;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.wep.challenge.domain.QChallenge.challenge;
import static com.example.onedaypiece.wep.challengeDetail.domain.QChallengeDetail.challengeDetail;

@RequiredArgsConstructor
@Repository
public class ChallengeDetailQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChallengeDetail> findAllByChallenge(List<Challenge> challengeList) {
        return queryFactory.selectFrom(challengeDetail)
                .join(challengeDetail.challenge,challenge)
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.in(challengeList))
                .fetch();
    }

    public List<ChallengeDetail> findAllByProgress(Long progress) {
        return queryFactory
                .selectFrom(challengeDetail)
                .join(challengeDetail.challenge, challenge).fetchJoin()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.eq(progress))
                .fetch();
    }

    public List<ChallengeDetail> findAllByChallengeList(List<Challenge> challengeList){
        return queryFactory.select(challengeDetail)
                .from(challengeDetail)

                .distinct()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.in(challengeList))
                .fetch();
    }

    public List<ChallengeDetail> findAllPopular(Pageable page) {
        return queryFactory.select(challengeDetail)
                .from(challengeDetail)
                .join(challengeDetail.challenge).fetchJoin()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.eq(1L),
                        challengeDetail.challenge.challengePassword.eq(""),
                        challengeDetail.challenge.viewCount.gt(0))
                .groupBy(challengeDetail.challenge.challengeId)
                .orderBy(challengeDetail.challenge.viewCount.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();
    }

    public List<ChallengeDetail> findAllByStatusTrue() {
        return queryFactory.selectFrom(challengeDetail)
                .join(challengeDetail.challenge).fetchJoin()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.eq(1L),
                        challengeDetail.challenge.challengePassword.eq(""))
                .orderBy(challengeDetail.modifiedAt.desc())
                .fetch();
    }
}
