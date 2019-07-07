package com.example.demo.dao;

import com.example.demo.entity.AuthnCode;
import com.example.demo.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthnCodeRepository extends JpaRepository<AuthnCode, Integer> {
    AuthnCode findAuthnCodeByActualCode(String actualCode);
}
