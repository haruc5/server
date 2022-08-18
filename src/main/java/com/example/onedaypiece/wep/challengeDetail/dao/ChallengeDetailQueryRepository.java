package com.example.onedaypiece.wep.challengeDetail.dao;

import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challenge.domain.ChallengeCategory;
import com.example.onedaypiece.wep.challengeDetail.domain.ChallengeDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.wep.challengeDetail.domain.QChallengeDetail.challengeDetail;

@RequiredArgsConstructor
@Repository
public class ChallengeDetailQueryRepository {

    private final JPAQueryFactory queryFactory;

//    private List<ChallengeDetail> findAllByChallenge(List<Challenge> challengeList) {
//        return queryFactory.selectFrom(challengeDetail)
//                .join(challengeDetail.challenge,challenge)
//                .where(challengeDetail.challengeDetailStatus.isTrue(),
//                        challengeDetail.challenge.challengeStatus.isTrue(),
//                        challengeDetail.challenge.in(challengeList))
//                .fetch();
//    }

    public List<ChallengeDetail> findAllByChallengeList(Slice<Challenge> challengeList){
        return queryFactory.select(challengeDetail)
                .from(challengeDetail)

                .distinct()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.in(challengeList.getContent()))
                .fetch();
    }

    public List<ChallengeDetail> findAllPopular(Pageable page) {
        return queryFactory.select(challengeDetail)
                .from(challengeDetail)
                .join(challengeDetail.challenge).fetchJoin()
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.eq(1),
                        challengeDetail.challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                        challengeDetail.challenge.challengePassword.eq(""))
                .groupBy(challengeDetail.challenge.challengeId)
                .orderBy(challengeDetail.challenge.challengeId.count().desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();
    }

    public List<ChallengeDetail> findAllByStatusTrue() {
        return queryFactory.selectFrom(challengeDetail)
                .join(challengeDetail.challenge).fetchJoin()

                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.challengeProgress.eq(1),
                        challengeDetail.challenge.challengePassword.eq(""))
                .orderBy(challengeDetail.modifiedAt.desc())
                .fetch();
    }

    public Long countByChallenge(Challenge challenge) {
        return queryFactory
                .select(challengeDetail.challengeDetailId)
                .from(challengeDetail)
                .where(challengeDetail.challengeDetailStatus.isTrue(),
                        challengeDetail.challenge.challengeStatus.isTrue(),
                        challengeDetail.challenge.eq(challenge))
                .fetchCount();
    }
}
