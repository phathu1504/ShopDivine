package com.example.ASM.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerID;
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private boolean enabled;
    private Date createDate;

    @OneToMany(mappedBy = "customer")
    private Set<AddressCustomer> addresses;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;
}
