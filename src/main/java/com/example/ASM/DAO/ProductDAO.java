package com.example.ASM.DAO;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ASM.Entity.Product;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product,Integer> {
    List<Product> findByCategory_CategoryID(int categoryID);
}
