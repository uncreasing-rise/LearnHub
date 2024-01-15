package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Integer paymentId;

    @Column(name = "PaymentAmount")
    private BigDecimal paymentAmount;

    @Column(name = "PaymentTime")
    private Date paymentTime;

    @Column(name = "PaymentStatus")
    private String paymentStatus;

    @Column(name = "PaymentType")
    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

}
