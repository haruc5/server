package com.example.onedaypiece.wep.challengeDetail.domain;

import com.example.onedaypiece.commom.Timestamped;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@ToString(exclude = {"challenge"})
@Table(indexes = {@Index(name = "idx_detail_status", columnList = "challenge_detail_status")})
public class ChallengeDetail extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer challengeDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "challenge_detail_status", nullable = false)
    private boolean challengeDetailStatus;

    @Builder
    public ChallengeDetail(Challenge challenge) {
        this.challenge = challenge;
        this.challengeDetailStatus = true;
    }

    public static ChallengeDetail createChallengeDetail(Challenge challenge){
        return ChallengeDetail.builder()
                .challenge(challenge)
                .build();
    }

    public void setStatusFalse() {
        this.challengeDetailStatus = false;
    }
}
