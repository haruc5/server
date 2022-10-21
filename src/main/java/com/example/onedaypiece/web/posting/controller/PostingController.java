package com.example.onedaypiece.web.posting.controller;

import com.example.onedaypiece.web.posting.domain.CreatePostingDto;
import com.example.onedaypiece.web.posting.domain.PostingListDto;
import com.example.onedaypiece.web.posting.domain.UpdatePostingDto;
import com.example.onedaypiece.web.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
public class PostingController {
    private final PostingService postingService;

    @Value("${resource.path}")
    private String path;

    @PostMapping("{challengeId}/create")
    public String createPosting(@RequestPart(value = "createPostingDto") CreatePostingDto createPostingDto,
                                @RequestPart(value = "imageSrc") MultipartFile imageSrc,
                                @PathVariable Integer challengeId) throws IOException {
        String fileName = imageSrc.getOriginalFilename();

        if(!new File(path).exists()) {
            try {
                new File(path).mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String saveFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String filePath = path + File.separator + saveFileName;
        imageSrc.transferTo(new File(filePath));

        createPostingDto.setPostingImg(saveFileName);
        postingService.createPosting(createPostingDto);
        return "challenge No." + challengeId + " posting completed";
    }

    //포스트 리스트
    @GetMapping("/list/{page}/{challengeId}")
    public PostingListDto getPosting(@PathVariable int page, @PathVariable Integer challengeId) {
        return postingService.getPosting(page, challengeId);
    }

    //포스트 업데이트
    @PutMapping("/{postingId}/update")
    public Integer updatePosting(@PathVariable Integer postingId,
                                 @RequestBody UpdatePostingDto updatePostingDto) {
        return postingService.updatePosting(postingId, updatePostingDto);
    }

    //포스트 삭제
    @DeleteMapping("/{page}/{challengeId}/{postingId}/delete")
    public Integer deletePosting(@PathVariable int page, @PathVariable Integer challengeId, @PathVariable Integer postingId) {
        postingService.getPosting(page, challengeId);
        return postingService.deletePosting(postingId);
    }



    @PostMapping("/upload")
    public ResponseEntity upload(@RequestPart MultipartFile image) {
        String fileName = image.getOriginalFilename();

        String saveFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String filePath = path + File.separator + saveFileName;

        System.out.println(path);
        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fileName);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(fileName);
    }
}
