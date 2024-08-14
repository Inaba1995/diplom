package data.entity;

import lombok.Data;

@Data
public class Payment {
    private String id;
    private String amount;
    private String created;
    private String status;
    private String transaction_id;
}
