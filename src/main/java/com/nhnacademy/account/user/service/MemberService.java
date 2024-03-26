package com.nhnacademy.account.user.service;

import com.nhnacademy.account.user.dto.LoginResponseDto;

public interface MemberService {

    LoginResponseDto getMemberIdAndPassword(String id);

}
