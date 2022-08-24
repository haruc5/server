package com.example.onedaypiece.wep.challenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ChallengeMainDto {
    private List<ChallengeSourceDto> slider = new ArrayList<>();
    private List<ChallengeSourceDto> popular = new ArrayList<>();
    private List<ChallengeSourceDto> exercise = new ArrayList<>();
    private List<ChallengeSourceDto> livinghabits = new ArrayList<>();
    private List<ChallengeSourceDto> nodrink = new ArrayList<>();
    private List<ChallengeSourceDto> nosmoke = new ArrayList<>();

    @Builder
    public ChallengeMainDto(List<ChallengeSourceDto> slider,
                            List<ChallengeSourceDto> popular,
                            List<ChallengeSourceDto> exercise,
                            List<ChallengeSourceDto> livinghabits,
                            List<ChallengeSourceDto> nodrink,
                            List<ChallengeSourceDto> nosmoke) {
        this.slider = slider;
        this.popular = popular;
        this.exercise = exercise;
        this.livinghabits = livinghabits;
        this.nodrink = nodrink;
        this.nosmoke = nosmoke;
    }

    public static ChallengeMainDto createChallengeMainDto(List<ChallengeSourceDto> slider,
                                                          List<ChallengeSourceDto> popular,
                                                          List<ChallengeSourceDto> exercise,
                                                          List<ChallengeSourceDto> livinghabits,
                                                          List<ChallengeSourceDto> nodrink,
                                                          List<ChallengeSourceDto> nosmoke) {
        return ChallengeMainDto.builder()
                .slider(slider)
                .popular(popular)
                .exercise(exercise)
                .livinghabits(livinghabits)
                .nodrink(nodrink)
                .nosmoke(nosmoke)
                .build();
    }
}
