package ngthu.com.Laptop_shop.controller;


import jakarta.servlet.http.HttpSession;
import ngthu.com.Laptop_shop.model.Category;
import ngthu.com.Laptop_shop.model.Product;
import ngthu.com.Laptop_shop.model.UserDtls;
import ngthu.com.Laptop_shop.repository.ProductRepository;
import ngthu.com.Laptop_shop.service.CategoryService;
import ngthu.com.Laptop_shop.service.ProductService;
import ngthu.com.Laptop_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
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

    @PostMapping("/saveUser")
    public  String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
      String imageName =  file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
      user.setProfileImage(imageName);
     UserDtls saveUser = userService.saveUser(user);

      if(!ObjectUtils.isEmpty(saveUser)){
          if(!file.isEmpty()){
              File saveFile = new ClassPathResource("static/img").getFile();

              Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                      + file.getOriginalFilename());

              System.out.println(path);
              Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
          }

          session.setAttribute("succMsg", "Register successfully");

      }else {
          session.setAttribute("errorMsg", "something wrong on server");

      }

        return "redirect:/register";
    }

}