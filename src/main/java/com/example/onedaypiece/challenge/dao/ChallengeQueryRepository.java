package com.example.onedaypiece.challenge.dao;

import com.example.onedaypiece.challenge.domain.challenge.Challenge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.onedaypiece.challenge.domain.challenge.QChallenge.challenge;
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
}
