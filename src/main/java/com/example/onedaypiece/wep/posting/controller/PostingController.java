package com.example.onedaypiece.wep.posting.controller;

import com.example.onedaypiece.wep.posting.domain.CreatePostingDto;
import com.example.onedaypiece.wep.posting.domain.PostingListDto;
import com.example.onedaypiece.wep.posting.domain.UpdatePostingDto;
import com.example.onedaypiece.wep.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
public class PostingController {
    private final PostingService postingService;

    //    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
//
//    @GetMapping("/uploadimage") public String displayUploadForm() {
//        return "imageupload/index";
//    }
//
//    @PostMapping("/upload") public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
//        StringBuilder fileNames = new StringBuilder();
//        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
//        fileNames.append(file.getOriginalFilename());
//        Files.write(fileNameAndPath, file.getBytes());
//        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
//        return "imageupload/index";
//    }
    @PostMapping("{challengeId}/create")
    public String createPosting(@RequestBody @Valid CreatePostingDto createPostingDto,
                                @RequestBody Map<String, Object> param,
                                @PathVariable Integer challengeId) {
        try {
            String path = null;
            String image = (String) param.get("cover");
            String imageName = (String) param.get(createPostingDto.getPostingImg());

            if (image != "") {
                UUID uuid = UUID.randomUUID();
                path = "c:\\work" + "" + uuid + "_" + image;

                makeFileWithString(image, imageName, uuid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static void makeFileWithString(String base64, String image, UUID uuid) {
        byte decode[] = Base64.decodeBase64(base64);
        FileOutputStream fos;
        try {
            File target = new File("c:\\work" + "" + uuid + "_" + image);
            target.createNewFile();
            fos = new FileOutputStream(target);
            fos.write(decode);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
