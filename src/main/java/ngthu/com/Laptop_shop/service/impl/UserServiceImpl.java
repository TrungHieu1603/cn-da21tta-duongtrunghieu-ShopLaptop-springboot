package ngthu.com.Laptop_shop.service.impl;

import ngthu.com.Laptop_shop.model.UserDtls;
import ngthu.com.Laptop_shop.repository.UserRepository;
import ngthu.com.Laptop_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDtls saveUser(UserDtls user) {

       UserDtls saveUser = userRepository.save(user);


        return saveUser;
    }
}
