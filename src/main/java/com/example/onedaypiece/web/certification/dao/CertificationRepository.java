package com.example.onedaypiece.web.certification.dao;

import com.example.onedaypiece.web.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {

}
