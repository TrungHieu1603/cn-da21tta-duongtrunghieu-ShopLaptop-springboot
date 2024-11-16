package ngthu.com.Laptop_shop.repository;

import ngthu.com.Laptop_shop.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDtls,Integer> {


}
