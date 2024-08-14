package data.entity;

import lombok.Data;

@Data
public class CreditRequest {
    private String id;
    private String bank_id;
    private String created;
    private String status;
}
