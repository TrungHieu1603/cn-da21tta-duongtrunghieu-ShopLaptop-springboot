package ngthu.com.Laptop_shop.repository;

import ngthu.com.Laptop_shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
