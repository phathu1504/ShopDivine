package com.example.ASM.DAO;

import com.example.ASM.Entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Page<Customer> findAll(Pageable pageable);
    Page<Customer> findByFullNameContaining(String keyword, PageRequest of);

    List<Customer> findByFullNameContaining(String keyword);
    Optional<Customer> findByEmailLike(String email);
    Customer findByEmail(String email);
    Customer findByFullName(String fullName);

}
