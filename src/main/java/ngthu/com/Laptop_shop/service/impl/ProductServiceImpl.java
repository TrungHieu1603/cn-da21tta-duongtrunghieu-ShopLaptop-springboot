package ngthu.com.Laptop_shop.service.impl;

import ngthu.com.Laptop_shop.model.Product;
import ngthu.com.Laptop_shop.repository.ProductRepository;
import ngthu.com.Laptop_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
