package com.example.onedaypiece.wep.posting.dao;

import com.example.onedaypiece.wep.posting.domain.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Integer> {
    @Modifying(clearAutomatically = true)
    @Query("update Posting p set p.postingModifyOk = false where p.postingId in :postingIdList")
    int updatePostingModifyOk(List<Integer> postingIdList);

    @Modifying(clearAutomatically = true)
    @Query("update Posting p set p.postingApproval = true  where p in :updatePostingId")
    int updatePostingApproval(List<Posting> updatePostingId);
}
