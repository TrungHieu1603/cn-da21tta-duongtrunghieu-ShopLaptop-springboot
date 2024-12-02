package ngthu.com.Laptop_shop.service;

import ngthu.com.Laptop_shop.model.Review;

import java.util.List;

public interface ReviewService {

    // Phương thức lưu đánh giá
    Review saveReview(Review review);

    // Phương thức lấy các đánh giá của sản phẩm
    List<Review> getReviewsByProductId(Integer productId);

    // Phương thức lấy các đánh giá của người dùng
    List<Review> getReviewsByUserId(Integer userId);
}
