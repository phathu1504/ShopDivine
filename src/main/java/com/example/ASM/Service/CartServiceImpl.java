package com.example.ASM.Service;

import com.example.ASM.DAO.CartItemDAO;
import com.example.ASM.Entity.CartItem;
import com.example.ASM.Entity.Customer;
import com.example.ASM.Entity.Product;
import com.example.ASM.Service.CartService;
import com.example.ASM.Service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.List;

@SessionScope
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemDAO cartItemDAO;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SessionService sessionService;

    private Customer getCustomerIdFromSession() {
        HttpSession session = request.getSession();
        Customer loggedInCustomer = (Customer) session.getAttribute("loggedIn");
        return loggedInCustomer;
    }

    @Override
    public Product add(Integer productId) {
        Customer customer = getCustomerIdFromSession();
        if (customer != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                CartItem cartItem = cartItemDAO.findByProductAndCustomer(product, customer);
                if (cartItem == null) {
                    cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(1);
                    cartItem.setCustomer(customer);
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                }
                cartItemDAO.save(cartItem);
                return product;
            }
        }
        return null;
    }

    @Override
    public void remove(Integer productId) {
        Customer customer = getCustomerIdFromSession();
        if (customer != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                CartItem cartItem = cartItemDAO.findByProductAndCustomer(product, customer);
                if (cartItem != null) {
                    cartItemDAO.delete(cartItem);
                }
            }
        }
    }


    @Override
    public Product update(Integer productId, String action) {
        Customer customer = getCustomerIdFromSession();
        if (customer != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                CartItem cartItem = cartItemDAO.findByProductAndCustomer(product, customer);
                if (cartItem != null) {
                    int quantity = cartItem.getQuantity();
                    if ("minus".equals(action) && quantity > 1) {
                        cartItem.setQuantity(quantity - 1);
                    } else if ("plus".equals(action)) {
                        cartItem.setQuantity(quantity + 1);
                    }
                    cartItemDAO.save(cartItem);
                    return product;
                }
            }
        }
        return null;
    }


    @Override
    public void clear() {
        cartItemDAO.deleteAll();
    }

    @Override
    public Collection<Product> getItems() {
        Customer loggedInCustomer = sessionService.get("loggedIn");
        List<CartItem> cartItems = cartItemDAO.findByCustomer(loggedInCustomer);
        return cartItems.stream().map(CartItem::getProduct).toList();
    }

    @Override
    public int getCount() {
        Customer loggedInCustomer = sessionService.get("loggedIn");
        List<CartItem> cartItems = cartItemDAO.findByCustomer(loggedInCustomer);
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public double getAmount() {
        Customer loggedInCustomer = sessionService.get("loggedIn");
        List<CartItem> cartItems = cartItemDAO.findByCustomer(loggedInCustomer);
        return cartItems.stream().mapToDouble(item -> item.getProduct().getPriceProduct() * item.getQuantity()).sum();
    }
}
