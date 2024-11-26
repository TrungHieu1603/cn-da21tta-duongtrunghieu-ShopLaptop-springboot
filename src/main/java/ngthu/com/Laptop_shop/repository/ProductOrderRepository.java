package ngthu.com.Laptop_shop.repository;

import ngthu.com.Laptop_shop.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer> {

    List<ProductOrder> findByUserId(Integer userId);

    ProductOrder findByOrderId(String orderId);

}
