package ngthu.com.Laptop_shop.controller;

import jakarta.servlet.http.HttpSession;
import ngthu.com.Laptop_shop.model.*;
import ngthu.com.Laptop_shop.service.*;
import ngthu.com.Laptop_shop.util.CommonUtil;
import ngthu.com.Laptop_shop.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService; //

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            m.addAttribute("countCart", countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);

        // Kiểm tra giỏ hàng trống và hiển thị thông báo
        if (carts.isEmpty()) {
            m.addAttribute("errorMsg", "Giỏ hàng của bạn hiện đang trống.");
        } else {
            if (carts.size() > 0) {
                Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
                m.addAttribute("totalOrderPrice", totalOrderPrice);
            }
        }

        return "/user/cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m, HttpSession session) {
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);

        // Kiểm tra giỏ hàng trống và hiển thị thông báo
        if (carts.isEmpty()) {
            session.setAttribute("errorMsg", "Bạn vẫn chưa chọn sản phẩm nào để mua.");
            return "redirect:/user/cart";  // Quay lại trang giỏ hàng nếu không có sản phẩm
        }

        if (carts.size() > 0) {
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
            m.addAttribute("orderPrice", orderPrice);
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p, HttpSession session) throws Exception {
        UserDtls user = getLoggedInUserDetails(p);

        // Kiểm tra giỏ hàng trống trước khi lưu đơn hàng
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        if (carts.isEmpty()) {
            session.setAttribute("errorMsg", "Bạn vẫn chưa chọn sản phẩm nào để mua.");
            return "redirect:/user/cart"; // Quay lại trang giỏ hàng nếu không có sản phẩm
        }

        orderService.saveOrder(user.getId(), request);

        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String loadSuccess() {
        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m, Principal p) {
        UserDtls loginUser = getLoggedInUserDetails(p);
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());
        m.addAttribute("orders", orders);
        return "/user/my_orders";
    }

    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Status Updated");
        } else {
            session.setAttribute("errorMsg", "Status not updated");
        }
        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile() {
        return "/user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img, HttpSession session) {
        UserDtls updateUserProfile = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(updateUserProfile)) {
            session.setAttribute("errorMsg", "Profile not updated");
        } else {
            session.setAttribute("succMsg", "Profile Updated");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
                                 HttpSession session) {
        UserDtls loggedInUserDetails = getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

        if (matches) {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            UserDtls updateUser = userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)) {
                session.setAttribute("errorMsg", "Password not updated !! Error in server");
            } else {
                session.setAttribute("succMsg", "Password Updated successfully");
            }
        } else {
            session.setAttribute("errorMsg", "Current Password incorrect");
        }

        return "redirect:/user/profile";
    }


    @PostMapping("/submit-review")
    public String submitReview(@RequestParam Integer productId,
                               @RequestParam Double rating,
                               @RequestParam String comment,
                               Principal p,
                               HttpSession session, Model m, RedirectAttributes redirectAttributes) {
        // Lấy thông tin người dùng đã đăng nhập
        String email = p.getName();
        UserDtls user = userService.getUserByEmail(email);

        // Kiểm tra xem người dùng có đơn hàng đã hoàn tất cho sản phẩm này không
        List<ProductOrder> orders = orderService.getOrdersByUser(user.getId());
        boolean hasPurchased = false;

        for (ProductOrder order : orders) {
            if (order.getProduct().getId().equals(productId) && order.getStatus().equals(OrderStatus.ORDER_RECEIVED.getName())) {
                hasPurchased = true;
                break;
            }
        }

        // Nếu người dùng chưa mua sản phẩm, hiển thị thông báo lỗi và không cho phép đánh giá
        if (!hasPurchased) {
            session.setAttribute("errorMsg", "Bạn phải mua sản phẩm trước khi đánh giá.");
            return "redirect:/product/" + productId;  // Quay lại trang sản phẩm
        }

        // Tạo và lưu đánh giá
        Review review = new Review();
        review.setUser(user);
        review.setProduct(productService.getProductById(productId));
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(new Date());

        // Lưu đánh giá vào cơ sở dữ liệu
        reviewService.saveReview(review);

        // Hiển thị thông báo thành công
        session.setAttribute("succMsg", "Bình luận của bạn đã được gửi thành công!");

        // Lấy lại thông tin sản phẩm và danh sách đánh giá
        Product product = productService.getProductById(productId);
        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        // Đảm bảo rằng reviews không rỗng
        if (reviews == null || reviews.isEmpty()) {
            session.setAttribute("errorMsg", "Không có đánh giá cho sản phẩm này.");
        }

        // Thêm sản phẩm và danh sách đánh giá vào model
        m.addAttribute("product", product);
        m.addAttribute("reviews", reviews);

        // Chuyển hướng lại trang sản phẩm và giữ lại thông tin
        redirectAttributes.addFlashAttribute("product", product);
        redirectAttributes.addFlashAttribute("reviews", reviews);

        return "redirect:/product/" + productId;
    }


}
