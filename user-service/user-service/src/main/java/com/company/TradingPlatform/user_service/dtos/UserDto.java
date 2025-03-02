package com.company.TradingPlatform.user_service.dtos;


import com.company.TradingPlatform.user_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role = Role.USER;   // default to USER
}
