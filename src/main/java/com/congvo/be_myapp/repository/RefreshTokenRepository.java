package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.RefreshToken;
import com.congvo.be_myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    @Modifying
    int deleteByUserEmail(User user);

    void deleteByToken(String token);
}
