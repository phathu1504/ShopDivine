package com.example.ASM.Service;

import com.example.ASM.Entity.Product;

import java.util.Collection;

public interface CartService {
    Product add(Integer id);
    void remove(Integer id);
    Product update(Integer id, String qty);
    void clear();
    Collection<Product> getItems();
    int getCount();
    double getAmount();
}
