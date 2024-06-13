package com.example.ASM.DAO;

import com.example.ASM.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffDAO extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByEmailLike(String email);

    Staff findByEmail(String email);
}
