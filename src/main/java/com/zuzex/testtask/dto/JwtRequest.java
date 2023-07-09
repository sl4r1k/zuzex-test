package com.zuzex.testtask.dto;

import lombok.Value;

@Value
public class JwtRequest {
    String username;
    String password;
}
