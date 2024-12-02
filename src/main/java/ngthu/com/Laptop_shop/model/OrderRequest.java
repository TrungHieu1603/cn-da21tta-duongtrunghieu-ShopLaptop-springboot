package ngthu.com.Laptop_shop.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

    private String fullName;

    private String email;

    private String mobileNo;

    private String address;


    private String paymentType;
}
