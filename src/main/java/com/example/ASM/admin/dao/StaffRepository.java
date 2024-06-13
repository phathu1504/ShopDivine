package com.example.ASM.admin.dao;

import com.example.ASM.Entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    @Query("SELECT s FROM Staff s WHERE s.email = :email")
    public Staff findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Staff s WHERE CONCAT(s.staffID, ' ', s.email, ' ', s.fullName, ' ', s.phone) LIKE %?1%")
    public List<Staff> findAll(String keyword);

    @Query("SELECT u FROM Staff u WHERE CONCAT(u.staffID, ' ', u.email, ' ', u.fullName, ' ', u.enabled, ' ', u.phone, ' ', u.createDate, ' ', u.address) LIKE %?1%")
    public Page<Staff> findAll(String keyword, Pageable pageable);
}
