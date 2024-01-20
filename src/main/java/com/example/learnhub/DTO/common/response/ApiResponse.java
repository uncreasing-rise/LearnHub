package com.example.learnhub.DTO.common.response;


import com.example.learnhub.DTO.common.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class ApiResponse<T> {
    private StatusEnum status;
    private T payload;

    private Object message;
    private HashMap<String, Object> metadata;

    public void ok() {
        this.status = StatusEnum.SUCCESS;
    }

    public ApiResponse ok(T data) {
        this.status = StatusEnum.SUCCESS;
        this.payload = data;
        return this;
    }
    public void okv2(Object message) {
        this.status = StatusEnum.SUCCESS;
        this.message = message;
    }

    public void ok(T data, HashMap<String, Object> metadata) {
        this.status = StatusEnum.SUCCESS;
        this.payload = data;
        this.metadata = metadata;
    }

    public void error(Object message) {
        this.status = StatusEnum.ERROR;
        this.message = message;
    }

}

