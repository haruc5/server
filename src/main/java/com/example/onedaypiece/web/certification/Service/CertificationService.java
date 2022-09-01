package com.example.onedaypiece.web.certification.Service;

import com.example.onedaypiece.exception.ApiRequestException;
import com.example.onedaypiece.web.certification.dao.CertificationRepository;
import com.example.onedaypiece.web.certification.domain.Certification;
import com.example.onedaypiece.web.certification.domain.CertificationDto;
import com.example.onedaypiece.web.posting.dao.PostingRepository;
import com.example.onedaypiece.web.posting.domain.Posting;
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

        Certification certification = Certification.createCertification(posting);

        certificationRepository.save(certification);


        return posting.getPostingId();
    }


    private Posting getPosting(Integer postingId) {
        return postingRepository.findById(postingId)
                .orElseThrow(() -> new ApiRequestException("등록된 포스트가 없습니다."));
    }
}
