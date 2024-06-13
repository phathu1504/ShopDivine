package com.example.ASM.admin.service;

import com.example.ASM.admin.dao.CategoryRepository;
import com.example.ASM.Entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).get();
    }

    public void delete(Integer id) {
        Category category = this.categoryRepository.findById(id).get();
        categoryRepository.delete(category);
    }

    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

}
