package com.nhnacademy.account.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequestDto {
    private String id;
    private String password;
    private String email;
}
