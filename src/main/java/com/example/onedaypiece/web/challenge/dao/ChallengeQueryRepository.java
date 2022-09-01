package com.example.onedaypiece.web.challenge.dao;

import com.example.onedaypiece.web.challenge.domain.Challenge;
import com.example.onedaypiece.web.challenge.domain.ChallengeCategory;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.onedaypiece.web.challenge.domain.QChallenge.challenge;
import static com.example.onedaypiece.web.challengeDetail.domain.QChallengeDetail.challengeDetail;

@RequiredArgsConstructor
@Repository
public class ChallengeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Challenge> findById(Integer challengeId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(challenge)
                .where(challenge.challengeId.eq(challengeId))
                .fetchOne());
    }

    public Long deleteAllStatusFalse() {
        return queryFactory
                .delete(challenge)
                .where(challenge.challengeStatus.isFalse())
                .execute();
    }

    public List<Challenge> findAllByWord(String words){
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .distinct()
                .join(challengeDetail).on(challenge.challengeId.eq(challengeDetail.challenge.challengeId),
                        challengeDetail.challengeDetailStatus.isTrue())
                .where(
                        challengeDetail.challengeDetailStatus.isTrue(),
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3),
                        challenge.challengeTitle.contains(words))
                .orderBy(challengeDetail.createdAt.asc())
                .fetch();

        return challengeList;
    }

    public List<Challenge> findAllBySearch(String word, String challengeCategory,
                                            int period, int progress) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .distinct().join(challengeDetail).on(challengeDetail.challengeDetailStatus.isTrue(),
                        challenge.challengeId.eq(challengeDetail.challenge.challengeId))
                .where(predicateByCategoryAndPeriod(word, challengeCategory, String.valueOf(period), progress))
                .orderBy(challenge.challengeProgress.asc(),
                        challengeDetail.createdAt.asc())
                .fetch();

        return challengeList;
    }

    private Predicate[] predicateByCategoryAndPeriod(String word, String challengeCategory,
                                                         String period, Integer progress) {
        Predicate[] predicates;
        if (word.equals("ALL")) {
            if (progress == 0) {
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory))
                    };
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.weekTag.eq(getPeriodString(period))
                    };
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                    };
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.weekTag.eq(getPeriodString(period))
                    };
                }
            } else {
                Long longNum = (long) progress;
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory))};
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.weekTag.eq(getPeriodString(period))};
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),};
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.weekTag.eq(getPeriodString(period))};
                }
            }
        } else {
            if (progress == 0) {
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3L),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3L),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3L),
                            challenge.challengeTitle.contains(word)
                    };
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3L),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                }
            } else {
                Long longNum = (long) progress;
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.challengeTitle.contains(word)
                    };
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(longNum),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                }
            }
        }
        return predicates;
    }

    private String getPeriodString(String period) {
        return period + "주";
    }
}
