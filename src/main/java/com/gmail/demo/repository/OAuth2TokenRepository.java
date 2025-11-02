package com.gmail.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmail.demo.entity.OAuth2Token;

public interface OAuth2TokenRepository extends JpaRepository<OAuth2Token, Long> {
    OAuth2Token findByUserEmail(String email);
    
    void deleteByUserEmail(String userEmail);
}
