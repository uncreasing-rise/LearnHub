package com.example.learnhub.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class PaymentDTO {
    private Integer paymentId;
    private Double paymentAmount;
    private Date paymentTime;
    private String paymentStatus;
    private String paymentType;
    private Integer userId;

}
