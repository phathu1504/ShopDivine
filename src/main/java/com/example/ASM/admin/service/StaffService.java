package com.example.ASM.admin.service;


import com.example.ASM.Entity.Staff;
import com.example.ASM.admin.dao.StaffRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class StaffService {

    private StaffRepository staffRepository;
    public static final int Staff_Per_Page = 10;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public Page<Staff> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, Staff_Per_Page, sort);

        if (keyword != null) {
            return staffRepository.findAll(keyword, pageable);
        }

        return staffRepository.findAll(pageable);
    }

    public Boolean findByEmail(String email) {
        if (staffRepository.findByEmail(email) == null) {
            return false;
        }
        return true;
    }


    public Staff findById(int id) {
        return staffRepository.findById(id).get();
    }

    public Staff saveStaff(Staff staff) {
        staff.setCreateDate(new Date());
        return staffRepository.save(staff);
    }

    public void deleteStaff(Staff staff) {
        staffRepository.delete(staff);
    }
}
