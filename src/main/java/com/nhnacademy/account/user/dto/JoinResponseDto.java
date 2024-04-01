package com.nhnacademy.account.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinResponseDto {
    private String id;
    private String password;
    private String email;
}
