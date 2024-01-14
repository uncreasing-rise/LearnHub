package com.example.learnhub.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class CartDTO {
    private Integer cartId;
    private Double total;
    private Integer userId;
    private Date cartDate;


}
