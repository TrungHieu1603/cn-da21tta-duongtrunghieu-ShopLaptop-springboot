package trunghieu.com.Laptop_shop.service.impl;
import trunghieu.com.Laptop_shop.model.Review;
import trunghieu.com.Laptop_shop.repository.ReviewRepository;
import trunghieu.com.Laptop_shop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Lưu đánh giá vào cơ sở dữ liệu
    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Lấy tất cả đánh giá của một sản phẩm
    @Override
    public List<Review> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    // Lấy tất cả đánh giá của một người dùng
    @Override
    public List<Review> getReviewsByUserId(Integer userId) {
        return reviewRepository.findByUserId(userId);
    }
}
