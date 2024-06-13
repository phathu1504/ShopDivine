package com.example.ASM.Service;

import com.example.ASM.DAO.CustomerDao;
import com.example.ASM.DAO.StaffDAO;
import com.example.ASM.Entity.Customer;
import com.example.ASM.Entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private StaffDAO staffDAO;

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public Customer getCustomerById(int customerId) {
        Optional<Customer> optionalCustomer = customerDao.findById(customerId);
        return optionalCustomer.orElse(null);
    }

    public void saveOrUpdateCustomer(Customer customer) {
        customerDao.save(customer);
    }

    public void deleteCustomer(int customerId) {
        customerDao.deleteById(customerId);
    }

    public Customer getCustomerByEmail(String email) {
        return customerDao.findByEmail(email);
    }

    public Customer getCustomerByFullName(String fullName) {
        return customerDao.findByFullName(fullName);
    }
    public Staff getStaffByEmail(String email) {
        return staffDAO.findByEmail(email);
    }

    public void addCustomer(Customer customer) throws Exception {
        if (getCustomerByEmail(customer.getEmail()) != null || getStaffByEmail(customer.getEmail()) != null) {
            throw new Exception("Email đã tồn tại");
        }
        customerDao.save(customer);
    }}

