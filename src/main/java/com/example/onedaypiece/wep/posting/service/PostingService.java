package com.example.onedaypiece.wep.posting.service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.wep.certification.CertificationQueryDto;
import com.example.onedaypiece.wep.certification.CertificationQueryRepository;
import com.example.onedaypiece.wep.certification.CertificationRepository;
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

    //포스트 저장
    public Integer createPosting(CreatePostingDto createPostingDto) {
        Challenge challenge = getChallenge(createPostingDto.getChallengeId());
        Posting posting = Posting.createPosting(createPostingDto, challenge);
        LocalDateTime now = LocalDate.now().atStartOfDay();

        // 주말 여부 확인
        checkChallengeHoliday(now, challenge);

        postingRepository.save(posting);

        return posting.getPostingId();
    }

    //포스트 리스트
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

    private Challenge getChallenge(Integer challengeId) {
        return challengeRepository.findByChallengeStatusTrueAndChallengeId(challengeId)
                .orElseThrow(() -> new ApiRequestException("등록된 챌린지가 없습니다."));
    }

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
