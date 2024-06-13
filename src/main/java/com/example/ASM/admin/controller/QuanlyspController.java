package com.example.ASM.admin.controller;

import com.example.ASM.DAO.ProductDAO;
import com.example.ASM.Entity.Product;
import com.example.ASM.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/product")
public class QuanlyspController {

    private final ProductDAO productDAO;
    private ProductService productService;

    @Autowired
    public QuanlyspController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @GetMapping
    public String showProductPage(Model model) {
        List<Product> productList = productDAO.findAll();
        model.addAttribute("items", productList);
        model.addAttribute("item", new Product());
        return "admin/products/quanlysp";
    }
    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> productList = productDAO.findAll();
        model.addAttribute("items", productList);
        return "admin/products/quanlysp";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute("item") Product product,
                                @RequestParam("imageProduct") MultipartFile multipartFile,
                                RedirectAttributes redirectAttributes) {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setImageProduct("/images/products/" + fileName);
            saveImageFile(fileName, multipartFile);
        }
        product.setCreateDate(new Date());
        productDAO.save(product);
        redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công");
        return "redirect:/admin/products";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("item") Product product,
                                RedirectAttributes redirectAttributes) {
        Optional<Product> existingProductOptional = productDAO.findById(product.getProductID());

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setNameProduct(product.getNameProduct());
            existingProduct.setDescriptionProduct(product.getDescriptionProduct());
            existingProduct.setPriceProduct(product.getPriceProduct());
            productDAO.save(existingProduct);
            redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm");
        }
        return "redirect:/admin/quanlysp";
    }




    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        Optional<Product> productOptional = productDAO.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Product> productList = productDAO.findAll();
            model.addAttribute("items", productList);
            model.addAttribute("item", product);
            return "admin/products/quanlysp";
        } else {
            // Xử lý nếu không tìm thấy sản phẩm
            return "redirect:/admin/products";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        productDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công");
        return "redirect:/admin/products";
    }

    private void saveImageFile(String fileName, MultipartFile multipartFile) {
        try {
            Path uploadPath = Paths.get("src/main/resources/static/images/products");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            multipartFile.transferTo(filePath.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
