package com.auth.user_management.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public class ServiceResponse<T> {
    public T data;
    public List<T> pageData;
    public Integer code;
}
