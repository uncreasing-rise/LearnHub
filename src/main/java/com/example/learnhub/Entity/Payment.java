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

    @Column(name = "BankCode")
    private String bankCode;



    @Column(name = "OrderInfo")
    private String OrderInfo;

    @JoinColumn(name = "UserID")
    private int userId;

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {return bankCode;}

    public void setOrderInfo(String orderInfo) {this.OrderInfo = orderInfo;}
    public String getOrderInfo() {return OrderInfo;}

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUser() {
        return userId;
    }

}
