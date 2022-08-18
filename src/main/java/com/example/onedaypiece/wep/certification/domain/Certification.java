package com.example.onedaypiece.wep.certification.domain;


import com.example.onedaypiece.wep.posting.domain.Posting;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Certification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "certification_id")
    private Integer certificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @Builder
    public Certification(Posting posting) {
        this.posting = posting;
    }

    public static Certification createCertification(Posting posting) {
        Certification certification = Certification.builder()
                .posting(posting)
                .build();

        posting.addCount();
        return certification;
    }

}
