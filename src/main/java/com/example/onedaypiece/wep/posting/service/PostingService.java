package com.example.onedaypiece.wep.posting.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.wep.certification.dao.CertificationQueryRepository;
import com.example.onedaypiece.wep.certification.dao.CertificationRepository;
import com.example.onedaypiece.wep.certification.domain.CertificationQueryDto;
import com.example.onedaypiece.wep.challenge.dao.ChallengeRepository;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import com.example.onedaypiece.wep.posting.dao.PostingQueryRepository;
import com.example.onedaypiece.wep.posting.dao.PostingRepository;
import com.example.onedaypiece.wep.posting.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostingService {
    private final PostingRepository postingRepository;
    private final CertificationRepository certificationRepository;
    private final CertificationQueryRepository certificationQueryRepository;
    private final ChallengeRepository challengeRepository;
    private final PostingQueryRepository postingQueryRepository;

    //인증 저장
    public Integer createPosting(CreatePostingDto createPostingDto) {
        Challenge challenge = getChallenge(createPostingDto.getChallengeId());
        Posting posting = Posting.createPosting(createPostingDto, challenge);
        LocalDateTime now = LocalDate.now().atStartOfDay();

        // 주말 여부 확인
        checkChallengeHoliday(now, challenge);

        postingRepository.save(posting);

        return posting.getPostingId();
    }

    //인증 리스트
    public PostingListDto getPosting(Integer page, Integer challengeId) {

        Pageable pageable = PageRequest.of(page - 1, 6);

        Slice<PostingListQueryDto> postingList = postingQueryRepository.findPostingList(challengeId, pageable);

        List<CertificationQueryDto> certificationList = certificationQueryRepository.findAllByPosting(challengeId);

        List<PostingDto> postingResponseDtoList = postingList
                .stream()
                .map(posting -> PostingDto.of(posting, certificationList))
                .collect(Collectors.toList());

        return PostingListDto.createPostingListDto(postingResponseDtoList, postingList.hasNext());
    }

    //인증 업데이트
    public Integer updatePosting(Integer postingId, UpdatePostingDto updatePostingDto) {

        Posting posting = getPosting(postingId);
        posting.updatePosting(updatePostingDto);

        return posting.getPostingId();
    }

    //포스트 삭제
    public Integer deletePosting(Integer postingId) {
        Posting posting = getPosting(postingId);

        // 인증 검사.
        isApprovalTrue(posting);
        System.out.println("posting = " + posting.getPostingId());
        posting.updateApproval(false);
        posting.deletePosting();

        return posting.getPostingId();
    }
    private Posting getPosting(Integer postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }
    private Challenge getChallenge(Integer challengeId) {
        return challengeRepository.findByChallengeStatusTrueAndChallengeId(challengeId)
                .orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }

    //인증 게시물 확인
    private void isApprovalTrue(Posting posting) {
        // 혼자서 진행하는 챌린지의 경우 수정 할 수 있게 하기위해 postingCount의 값이 1이 아닐 경우로 한정
        if (posting.getPostingCount() != 1) {
            if (posting.isPostingApproval()) {
                throw new ApiRequestException("이미 인증된 게시글은 삭제할 수 없습니다.");
            }
        }
    }

    //주말 챌린지 작성 불가
    private void checkChallengeHoliday(LocalDateTime now, Challenge challenge) {

        boolean sat = 6 == now.getDayOfWeek().getValue();
        boolean sun = 7 == now.getDayOfWeek().getValue();

        if (sat || sun) {
            if (challenge.getChallengeHoliday().equals("0,6")) {
                throw new ApiRequestException("주말에 작성 불가능한 챌린지 입니다!");
            }
        }
    }
}
