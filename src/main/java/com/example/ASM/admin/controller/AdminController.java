package com.example.ASM.admin.controller;


import com.example.ASM.Entity.Staff;
import com.example.ASM.admin.export.StaffExcelExport;
import com.example.ASM.admin.export.StaffPDFExport;
import com.example.ASM.admin.service.CategoryService;
import com.example.ASM.admin.service.StaffService;
import com.example.ASM.admin.utils.FileUploadUtils;
import com.example.ASM.Entity.Category;
import com.example.ASM.Entity.Staff;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private StaffService staffService;
    private CategoryService categoryService;

    @Autowired
    public AdminController(StaffService staffService, CategoryService categoryService) {
        this.staffService = staffService;
        this.categoryService = categoryService;
    }

    //    ADMIN
    @GetMapping({"/", ""})
    public String showPageAdmin() {
        return "admin/index";
    }

    //    USER
//    @GetMapping("/user")
//    public String showPageUser(Model model) {
//        List<Staff> staffList = staffService.findAll();
//        model.addAttribute("staffList", staffList);
//        return "admin/user/users";
//    }

    @GetMapping("/user")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "staffID", "asc", null);
    }

    @GetMapping("/user/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, @Param("keyword") String keyword) {
        System.out.println(pageNum);
        System.out.println(sortDir);
        System.out.println(sortField);
        System.out.println(keyword);
        Page<Staff> page = staffService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Staff> staffList = page.getContent();

        long startCount = (pageNum - 1) * staffService.Staff_Per_Page + 1;
        long endCount = startCount + staffService.Staff_Per_Page - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("staffList", staffList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "admin/user/users";
    }

    @GetMapping("/user/create")
    public String showPageUserCreate(Model model) {
        Staff staff = new Staff();
        staff.setEnabled(true);
        model.addAttribute("staff", staff);
        model.addAttribute("message", "CREATE USER");
        return "admin/user/form_user";
    }

    @PostMapping("/user/save")
    public String saveUser(@Valid @ModelAttribute("staff") Staff staff,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws Exception {
        if (bindingResult.hasErrors()) {
            return "admin/user/form_user";
        }

        if (staffService.findByEmail(staff.getEmail()) && staff.getStaffID() == 0) {
            bindingResult.addError(new FieldError("staff", "email", "Email is exited"));
            return "admin/user/form_user";
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            staff.setImageName(fileName);
            Staff saveStaff = staffService.saveStaff(staff);
            String uploadDir = "staff-photos/" + saveStaff.getStaffID();

            FileUploadUtils.cleanDir(uploadDir);
            FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);

        } else {
            if (staff.getImageName().isEmpty()) {
                staff.setImageName(null);
            }
            staffService.saveStaff(staff);
        }

        redirectAttributes.addFlashAttribute("message", "Save successful");
        return "redirect:/admin/user";
    }


    @GetMapping("/user/edit/{id}")
    public String showPageUserEdit(@PathVariable("id") Integer idStaff, Model model) {
        Staff staff = staffService.findById(idStaff);
        model.addAttribute("staff", staff);
        model.addAttribute("message", "EDIT USER " + idStaff);
        return "admin/user/form_user";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteStaff(@PathVariable("id") Integer idStaff, RedirectAttributes redirectAttributes) {
        Staff staff = staffService.findById(idStaff);
        redirectAttributes.addFlashAttribute("message", "Delete user " + idStaff + " successfully");
        staffService.deleteStaff(staff);
        return "redirect:/admin/user";
    }

    @GetMapping("/user/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Staff> listStaff = staffService.findAll();
        StaffExcelExport exporter = new StaffExcelExport();
        exporter.export(listStaff, response);
    }

    @GetMapping("/user/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<Staff> listStaff = staffService.findAll();
        StaffPDFExport exporter = new StaffPDFExport();
        exporter.export(listStaff, response);
    }

//    END USER

//    Category
    @GetMapping("/categories")
    public String showPageCategories(Model model) {
        this.categoryService.findAll();
        model.addAttribute("categories", this.categoryService.findAll());
        return "admin/category/categories";
    }

    @GetMapping("/categories/create")
    public String showPageCategoriesCreate(Model model) {
        Category category = new Category();
        category.setEnabled(true);
        model.addAttribute("category", category);
        return "admin/category/form_category";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "admin/category/form_category";
        }

        System.out.println(category.toString());

        this.categoryService.save(category);

        redirectAttributes.addFlashAttribute("message", "Save successful");

        return "redirect:/admin/categories";
    }

    @GetMapping("categories/edit/{id}")
    public String showPageCategoriesEdit(@PathVariable("id") Integer idCategory, Model model) {
        Category category = this.categoryService.findById(idCategory);
        model.addAttribute("category", category);
        return "admin/category/form_category";
    }

    @GetMapping("categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer idCategory,
                                 RedirectAttributes redirectAttributes) {
        this.categoryService.delete(idCategory);
        redirectAttributes.addFlashAttribute("message", "Delete category " + idCategory + " successfully");
        return "redirect:/admin/categories";
    }

//    END CATEGORY


//    @GetMapping("/customer")
//    public String showPageCustomer() {
//        return "admin/customer/customers";
//    }

    @GetMapping("/order")
    public String showPageOrders() {
        return "admin/order/orders";
    }
}
