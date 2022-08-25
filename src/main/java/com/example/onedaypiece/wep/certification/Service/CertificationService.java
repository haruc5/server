package com.example.onedaypiece.wep.certification.Service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.wep.certification.dao.CertificationRepository;
import com.example.onedaypiece.wep.certification.domain.Certification;
import com.example.onedaypiece.wep.certification.domain.CertificationDto;
import com.example.onedaypiece.wep.posting.dao.PostingRepository;
import com.example.onedaypiece.wep.posting.domain.Posting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final PostingRepository postingRepository;

    @Transactional
    public Integer createCertification(CertificationDto certificationDto) {
        Posting posting = getPosting(certificationDto.getPostingId());
        Integer memberCount = certificationDto.getTotalNumber();
        Certification certification = Certification.createCertification(posting);

        certificationRepository.save(certification);
        checkPostingCount(posting, memberCount);
        return posting.getPostingId();
    }

    private void checkPostingCount (Posting posting, Integer memberCount) {
        if(memberCount / 2 <= posting.getPostingCount()){
            if(!posting.isPostingApproval()){
                posting.updateApproval(true);
            }
        }
    }
    private Posting getPosting(Integer postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }
}
