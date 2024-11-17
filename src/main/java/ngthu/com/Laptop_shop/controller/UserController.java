package ngthu.com.Laptop_shop.controller;

import ngthu.com.Laptop_shop.model.Category;
import ngthu.com.Laptop_shop.model.UserDtls;
import ngthu.com.Laptop_shop.service.CategoryService;
import ngthu.com.Laptop_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/")
    private String home(){
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p != null){
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user",userDtls);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys",allActiveCategory);


    }
}
