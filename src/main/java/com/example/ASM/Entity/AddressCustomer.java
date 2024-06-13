package com.example.ASM.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address_customer")
public class AddressCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressID;

    @ManyToOne
    @JoinColumn(name = "customerID", nullable = false)
    private Customer customer;

    private String fullName;
    private String phone;
    private String address;
    private boolean enabled;


}
