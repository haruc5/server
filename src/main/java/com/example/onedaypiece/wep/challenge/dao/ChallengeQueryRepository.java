package com.example.onedaypiece.wep.challenge.dao;

import com.example.onedaypiece.util.RepositoryHelper;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.challenge.domain.ChallengeCategory;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.onedaypiece.wep.challenge.domain.QChallenge.challenge;
import static com.example.onedaypiece.wep.challengeDetail.domain.QChallengeDetail.challengeDetail;

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

    public Slice<Challenge> findAllByWord(String words, Pageable page){
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .distinct()
                .join(challengeDetail).on(challenge.challengeId.eq(challengeDetail.challenge.challengeId),
                        challengeDetail.challengeDetailStatus.isTrue())
                .where(
                        challengeDetail.challengeDetailStatus.isTrue(),
                        challenge.challengeStatus.isTrue(),
                        challenge.challengeProgress.lt(3),
                        challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                        challenge.challengeTitle.contains(words))
                .orderBy(challenge.challengeStart.asc())
                .offset(page.getOffset())
                .limit(page.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, page);
    }

    public Slice<Challenge> findAllBySearch(String word, String challengeCategory,
                                            int period, int progress, Pageable pageable) {
        List<Challenge> challengeList = queryFactory
                .selectFrom(challenge)
                .distinct().join(challengeDetail).on(challengeDetail.challengeDetailStatus.isTrue(),
                        challenge.challengeId.eq(challengeDetail.challenge.challengeId))
                .where(predicateByCategoryAndPeriod(word, challengeCategory, String.valueOf(period), progress))
                .orderBy(challenge.challengeProgress.asc(),
                        challenge.challengeStart.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositoryHelper.toSlice(challengeList, pageable);
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
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory))
                    };
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period))
                    };
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL)
                    };
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period))
                    };
                }
            } else {
                Integer intNum = (int) progress;
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory))};
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period))};
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL)};
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period))};
                }
            }
        } else {
            if (progress == 0) {
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.challengeTitle.contains(word)
                    };
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.lt(3),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                }
            } else {
                Integer intNum = (int) progress;
                if (!challengeCategory.equals("ALL") && period.equals("0")) { // 카테고리o 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (!challengeCategory.equals("ALL")) { // 카테고리o, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.eq(ChallengeCategory.valueOf(challengeCategory)),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.weekTag.eq(getPeriodString(period)),
                            challenge.challengeTitle.contains(word)
                    };
                } else if (period.equals("0")) { // 카테고리x, 기간x
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
                            challenge.challengeTitle.contains(word)
                    };
                } else { // 카테고리x, 기간o
                    predicates = new Predicate[]{
                            challenge.challengeStatus.isTrue(),
                            challenge.challengeProgress.eq(intNum),
                            challenge.challengeCategory.ne(ChallengeCategory.OFFICIAL),
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
