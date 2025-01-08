package trunghieu.com.Laptop_shop.repository;

import trunghieu.com.Laptop_shop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProductId(Integer productId); // Lấy các bình luận cho một sản phẩm cụ thể

    List<Review> findByUserId(Integer userId); // Lấy các bình luận của một người dùng

}
