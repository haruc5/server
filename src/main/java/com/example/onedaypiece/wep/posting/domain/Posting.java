package com.example.onedaypiece.wep.posting.domain;

import com.example.onedaypiece.commom.Timestamped;
import com.example.onedaypiece.wep.challenge.domain.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Posting extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "posting_Id")
    private Integer postingId;

    @Column(columnDefinition="TEXT")
    private String postingImg;

    @Column(columnDefinition="TEXT")
    private String postingContent;

    @Column
    private boolean postingStatus;

    @Column
    private boolean postingApproval;

    @Column
    private boolean postingModifyOk;

    @Column
    private Integer postingCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Builder
    public Posting(String postingImg, String postingContent, Challenge challenge) {
        this.postingImg = postingImg;
        this.postingContent = postingContent;
        this.postingStatus = true;
        this.postingApproval=false;
        this.postingModifyOk = true;
        this.postingCount = 0;
        this.challenge = challenge;
    }

    public static Posting createPosting(CreatePostingDto createPostingDto, Challenge challenge) {
        return Posting.builder()
                .postingImg(createPostingDto.getPostingImg())
                .postingContent(createPostingDto.getPostingContent())
                .challenge(challenge)
                .build();
    }

    public void updatePosting(UpdatePostingDto updatePostingDto) {
        this.postingImg = updatePostingDto.getPostingImg();
        this.postingContent = updatePostingDto.getPostingContent();

    }
    public void deletePosting() {
        this.postingStatus =false;
    }

    public void updateApproval(boolean isApproval) {
        this.postingApproval = isApproval;
    }

    public void addCount() {
        this.postingCount += 1;
    }
}
