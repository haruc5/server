package com.example.onedaypiece.wep.posting.controller;

import com.example.onedaypiece.wep.posting.domain.CreatePostingDto;
import com.example.onedaypiece.wep.posting.domain.PostingListDto;
import com.example.onedaypiece.wep.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/detail")
public class PostingController {
    private final PostingService postingService;

    @PostMapping("/posting")
    public String createPosting(@RequestBody @Valid CreatePostingDto createPostingDto) {
        postingService.createPosting(createPostingDto);
        return "인증 완료";
    }

    //포스트 리스트
    @GetMapping("/list/{page}/{challengeId}")
    public PostingListDto getPosting(@PathVariable int page, @PathVariable Integer challengeId) {
        return postingService.getPosting(page, challengeId);
    }

}
