package com.company.TradingPlatform.user_service.dtos;


import lombok.Data;

@Data
public class AuthResponseDto {

    private String email;
    private String password;
}
