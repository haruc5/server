package com.example.onedaypiece.web.posting.dao;

import com.example.onedaypiece.util.RepositoryHelper;
import com.example.onedaypiece.web.posting.domain.PostingListQueryDto;
import com.example.onedaypiece.web.posting.domain.QPostingListQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.onedaypiece.web.posting.domain.QPosting.posting;

@RequiredArgsConstructor
@Repository
public class PostingQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<PostingListQueryDto> findPostingList(Integer challengeId, Pageable pageable){
        List<PostingListQueryDto> postingList = queryFactory.select(new QPostingListQueryDto(
                        posting.postingId,
                        posting.postingImg,
                        posting.postingContent,
                        posting.postingModifyOk,
                        posting.createdAt,
                        posting.modifiedAt,
                        posting.postingCount
                ))
                .from(posting)
                .join(posting.challenge)
                .where(posting.challenge.challengeId.eq(challengeId),
                        posting.postingStatus.isTrue())
                .orderBy(posting.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();

        return RepositoryHelper.toSlice(postingList,pageable);
    }
}
