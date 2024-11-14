package ngthu.com.Laptop_shop.repository;

import ngthu.com.Laptop_shop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    public  Boolean existsByName(String name);

}
