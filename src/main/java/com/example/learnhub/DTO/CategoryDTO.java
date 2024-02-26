package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName;

    public static class TransactionStatusDTO {
    }
}
