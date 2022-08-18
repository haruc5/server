package com.example.onedaypiece.wep.certification.controller;

import com.example.onedaypiece.wep.certification.Service.CertificationService;
import com.example.onedaypiece.wep.certification.domain.CertificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/certification")
@RestController
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("")
    public Integer createCertification(
            @RequestBody CertificationDto certificationDto){

        return certificationService.createCertification(certificationDto);
    }
}
