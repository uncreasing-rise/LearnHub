package com.example.learnhub.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer cartId;
    private Double total;
    private Integer userId;
    private Date cartDate;
}
