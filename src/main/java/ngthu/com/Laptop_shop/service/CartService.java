package ngthu.com.Laptop_shop.service;

import ngthu.com.Laptop_shop.model.Cart;

import java.util.List;

public interface CartService {

    public Cart saveCart(Integer productId, Integer userId);

    public List<Cart> getCartsByUser(Integer userId);

}