package com.example.ASM.Entity;

import com.example.ASM.Entity.Category;
import com.example.ASM.Entity.Order;
import com.example.ASM.Entity.Staff;
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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    @ManyToOne
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "staffID", nullable = false)
    private Staff staff;

    private String nameProduct;
    private double priceProduct;
    private String imageProduct;
    private String descriptionProduct;
    private boolean status;
    private Date createDate;

    @OneToMany(mappedBy = "product")
    private Set<Order> orders;

//    private String masp;
}

