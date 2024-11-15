package ngthu.com.Laptop_shop.controller;


import ngthu.com.Laptop_shop.model.Category;
import ngthu.com.Laptop_shop.model.Product;
import ngthu.com.Laptop_shop.repository.ProductRepository;
import ngthu.com.Laptop_shop.service.CategoryService;
import ngthu.com.Laptop_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/products")
    public String products(Model m) {
      List<Category> categories = categoryService.getAllActiveCategory();
      List<Product> products = productService.getAllActiveProducts();

      m.addAttribute("categories",categories);
      m.addAttribute("products",products);
      return "product";
    }

    @GetMapping("/product")
    public String product() {
        return "view_product";
    }

}