package com.example.onedaypiece.wep.posting.dao;

import com.example.onedaypiece.wep.posting.domain.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Integer> {

}
