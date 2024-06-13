package com.example.ASM.admin.controller;

import com.example.ASM.DAO.CustomerDao;
import com.example.ASM.Entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerController(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @GetMapping
    public String showPage(Model model) {
        Page<Customer> customerPage = customerDao.findAll(PageRequest.of(0, 5));
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", 0);
        return "admin/customer/customers";
    }

    @GetMapping("/customer")
    public String showCustomerList(Model model) {
        Page<Customer> customerPage = customerDao.findAll(PageRequest.of(0, 5));
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", 0);
        return "admin/customer/customers";
    }

    @GetMapping("/page")
    public String showCustomerListPage(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(required = false) String keyword,
                                       Model model) {
        Page<Customer> customerPage;
        if (keyword == null || keyword.isEmpty()) {
            customerPage = customerDao.findAll(PageRequest.of(page, size));
        } else {
            customerPage = customerDao.findByFullNameContaining(keyword, PageRequest.of(page, size));
        }
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "admin/customer/customers";
    }

    @GetMapping("/search")
    public String searchCustomerByName(@RequestParam String keyword, Model model) {
        Page<Customer> customerPage = customerDao.findByFullNameContaining(keyword, PageRequest.of(0, 5));
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", 0);
        model.addAttribute("keyword", keyword);
        return "admin/customer/customers";
    }
}
