package com.example.onedaypiece.web.certification.dao;

import com.example.onedaypiece.web.certification.domain.CertificationQueryDto;
import com.example.onedaypiece.web.certification.domain.QCertificationQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.certification.domain.QCertification.certification;

@RequiredArgsConstructor
@Repository
public class CertificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CertificationQueryDto> findAllByPosting(Integer challengeId){
        return queryFactory.select(new QCertificationQueryDto(certification.posting.postingId))
                .from(certification)
                .join(certification.posting)
                .where(certification.posting.challenge.challengeId.eq(challengeId))
                .fetch();
    }
}
