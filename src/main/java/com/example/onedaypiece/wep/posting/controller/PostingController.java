package com.example.onedaypiece.wep.posting.controller;

import com.example.onedaypiece.wep.posting.domain.CreatePostingDto;
import com.example.onedaypiece.wep.posting.domain.PostingListDto;
import com.example.onedaypiece.wep.posting.domain.UpdatePostingDto;
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
    //포스트 업데이트
    @PutMapping("/posting/update/{postingId}")
    public Integer updatePosting(@PathVariable Integer postingId,
                              @RequestBody UpdatePostingDto updatePostingDto){
        return postingService.updatePosting(postingId, updatePostingDto);
    }

    //포스트 삭제
    @DeleteMapping("/posting/delete/{postingId}")
    public Integer deletePosting(@PathVariable Integer postingId) {
        return postingService.deletePosting(postingId);
    }
}