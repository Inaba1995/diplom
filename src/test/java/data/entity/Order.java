package data.entity;


import lombok.Data;

@Data
public class Order {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;
}
