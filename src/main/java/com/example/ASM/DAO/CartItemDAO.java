package com.example.ASM.DAO;

import com.example.ASM.Entity.CartItem;
import com.example.ASM.Entity.Customer;
import com.example.ASM.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemDAO extends JpaRepository<CartItem,Long> {
    CartItem findByProductAndCustomer(Product product, Customer customer);
    List<CartItem> findByCustomer(Customer customer);
//    @Query("DELETE FROM CartItem c WHERE c.product = :product")
//    void delete(Product product);
}
