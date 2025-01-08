package trunghieu.com.Laptop_shop.config;

import trunghieu.com.Laptop_shop.model.UserDtls;
import trunghieu.com.Laptop_shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDtls user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new CustomUser(user);
    }

}