package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Data
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Integer cartId;

    @Column(name = "Total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "UserID")  // Assuming "accountId" is the foreign key column in the Cart table
    private User user;  // Change to the correct attribute name for the relationship

    @Column(name = "cart_date")
    private Date cartDate;

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCartDate(Date cartDate) {
        this.cartDate = cartDate;
    }
}
