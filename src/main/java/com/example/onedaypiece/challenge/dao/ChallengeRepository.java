package com.example.onedaypiece.challenge.dao;


import com.example.onedaypiece.challenge.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
}
