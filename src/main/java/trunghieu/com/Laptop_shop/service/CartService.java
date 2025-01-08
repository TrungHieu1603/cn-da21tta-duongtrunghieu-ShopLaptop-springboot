package trunghieu.com.Laptop_shop.service;

import trunghieu.com.Laptop_shop.model.Cart;

import java.util.List;

public interface CartService {

    public Cart saveCart(Integer productId, Integer userId);

    public List<Cart> getCartsByUser(Integer userId);

    public Integer getCountCart(Integer userId);

    public void updateQuantity(String sy, Integer cid);

    // Add this method to delete a product from the cart
    public void removeCartItem(Integer cartItemId);
}