package com.example.ASM.Controller;

import com.example.ASM.DAO.CartItemDAO;
import com.example.ASM.DAO.CustomerDao;
import com.example.ASM.DAO.StaffDAO;
import com.example.ASM.Entity.CartItem;
import com.example.ASM.Entity.Customer;
import com.example.ASM.Entity.Product;
import com.example.ASM.Entity.Staff;
import com.example.ASM.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    ProductService pbService;
    @Autowired
    CookieService cookieService;
    @Autowired
    ParamService paramService;
    @Autowired
    SessionService sessionService;
    @Autowired
    CartService cart;
    @Autowired
    CustomerDao customerDao;
    @Autowired
    CartItemDAO cartItemDAO;
    @Autowired
    CustomerService customerService;
    @Autowired
    StaffDAO staffDAO;

    @GetMapping("trangchu")
    public String trangChu(Model model) {
        setLoggedInAttribute(model);
        model.addAttribute("dsNb", pbService.getProductsByCategoryID(4));
        model.addAttribute("dsSteam", pbService.getProductsByCategoryID(5));
        model.addAttribute("dsHt", pbService.getProductsByCategoryID(2));
        model.addAttribute("dsGt", pbService.getProductsByCategoryID(1));
        model.addAttribute("dsLv", pbService.getProductsByCategoryID(3));
        model.addAttribute("dsSp", pbService.getAllProducts());
        return "user/index";
    }

    @RequestMapping("danhmucsp/{dm}")
    public String dm(Model model, @PathVariable("dm") int dm) {
        setLoggedInAttribute(model);
        model.addAttribute("dsSp", pbService.getProductsByCategoryID(dm));
        model.addAttribute("category", pbService.getCategoryName(dm));
        return "user/indexdm";
    }

    @RequestMapping("sp/{id}")
    public String chitiet(Model model, @PathVariable("id") int id) {
        setLoggedInAttribute(model);
        Product product = pbService.getProductById(id);
        model.addAttribute("sp", product);
        model.addAttribute("items", pbService.getProductById(id));
        model.addAttribute("count", cart.getCount());
        return "user/chitiet";
    }

    @RequestMapping("giohang")
    public String giohang(Model model) {
        setLoggedInAttribute(model);
        Customer loggedInCustomer = sessionService.get("loggedIn");
        List<CartItem> cartItems = cartItemDAO.findByCustomer(loggedInCustomer);
        model.addAttribute("cartItems", cartItems);
        double totalAmount = cart.getAmount();
        model.addAttribute("totalAmount", totalAmount);
        return "user/giohangview";
    }

    @RequestMapping("/sp/add/{id}")
    public String add(@PathVariable("id") Integer id, Model model) {
        if (checkLoggedIn()) {
            // Người dùng đã đăng nhập
            cart.add(id);
            return "redirect:/sp/{id}";
        } else {
            // Người dùng chưa đăng nhập
            model.addAttribute("message", "Vui lòng đăng nhập!");
            return "redirect:/login";
        }
    }

    @RequestMapping("/sp/remove/{id}")
    public String remove(@PathVariable("id") Integer id) {
        cart.remove(id);
        return "redirect:/giohang";
    }

    @RequestMapping("/sp/update/{id}/{pre}")
    public String update(@PathVariable("id") Integer id, @PathVariable("pre") String pre) {
        cart.update(id, pre);
        return "redirect:/giohang";
    }

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @PostMapping("login")
    public String login(Model model) {
        setLoggedInAttribute(model);
        String Email = paramService.getString("email", "");
        String password = paramService.getString("password", "");
        Optional<Customer> customerOptional = customerDao.findByEmailLike(Email);
        Optional<Staff> staffOptional = staffDAO.findByEmailLike(Email);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            // Kiểm tra mật khẩu
            if (password.equals(customer.getPassword())) {
                // Mật khẩu đúng, đăng nhập thành công
                sessionService.set("loggedIn", customer);
                return "redirect:/trangchu"; // Hoặc trang chính khác
            } else {
                // Mật khẩu không đúng
                model.addAttribute("message", "Sai mật khẩu");
                return "user/login";
            }
        } else if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            if (password.equals(staff.getPassword())) {
                sessionService.set("loggedIn", staff);
                return "admin/index";
            } else {
                model.addAttribute("message", "Sai mật khẩu");
            }
        } else {
            model.addAttribute("message", "Email không tồn tại");
            return "user/login";
        }
        return "user/login";
    }

    @RequestMapping("logout")
    public String logout(Model model) {
        sessionService.remove("loggedIn");
        model.addAttribute("loggedIn", false);
        return "redirect:/trangchu";
    }

    @GetMapping("register")
    public String register() {
        return "user/register";
    }

    @PostMapping("register")
    public String register(Model model) throws Exception {
        String email = paramService.getString("email", "");
        String password = paramService.getString("password", "");
        String fullName = paramService.getString("fullName", "");
        String phone = paramService.getString("phone", "");
        Customer existingCustomerByEmail = customerService.getCustomerByEmail(email);
        if (existingCustomerByEmail != null) {
            model.addAttribute("message", "Email đã tồn tại");
            return "user/register";
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setEmail(email);
            newCustomer.setPassword(password);
            newCustomer.setFullName(fullName);
            newCustomer.setEnabled(true);
            newCustomer.setCreateDate(new Date());
            newCustomer.setPhone(phone);
            customerService.addCustomer(newCustomer);
            return "redirect:/trangchu";
        }
    }

    public boolean checkLoggedIn() {
        Customer loggedInCustomer = sessionService.get("loggedIn");
        return loggedInCustomer != null;
    }

    private void setLoggedInAttribute(Model model) {
        Customer loggedInCtm = sessionService.get("loggedIn");
        model.addAttribute("loggedIn", loggedInCtm != null);
    }

}


