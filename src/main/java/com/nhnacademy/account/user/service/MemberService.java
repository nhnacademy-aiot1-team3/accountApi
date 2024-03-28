package com.nhnacademy.account.user.service;


import com.nhnacademy.account.user.dto.JoinRequestDto;
import com.nhnacademy.account.user.dto.JoinResponseDto;
import com.nhnacademy.account.user.dto.LoginInfoResponseDto;

public interface MemberService {

    LoginInfoResponseDto getMemberIdAndPassword(String id);

    JoinResponseDto createMember(JoinRequestDto request);
}
