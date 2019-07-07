package com.example.demo.dao;

import com.example.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    Manager findManagerByUsername(String username);
}
