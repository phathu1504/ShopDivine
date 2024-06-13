package com.example.ASM.Service;

import com.example.ASM.Entity.Product;
import com.example.ASM.DAO.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    @Autowired
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> getProductsByCategoryID(int categoryID) {
        return productDAO.findByCategory_CategoryID(categoryID);
    }

    public Product getProductById(int id) {
        return productDAO.findById(id).orElse(null);
    }

    public String getCategoryName(int category) {
        if (category == 1) {
            return "Giải trí";
        } else if (category == 3) {
            return "Làm việc";
        } else if (category == 2) {
            return "Học tập";
        } else if (category == 5) {
            return "Steam";
        } else {
            return String.valueOf(category);
        }
    }


}
