package ngthu.com.Laptop_shop.service;

import ngthu.com.Laptop_shop.model.OrderRequest;
import ngthu.com.Laptop_shop.model.ProductOrder;


public interface OrderService {

    public void saveOrder(Integer userid, OrderRequest orderRequest);
}
