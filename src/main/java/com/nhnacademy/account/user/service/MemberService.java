package com.nhnacademy.account.user.service;

import com.nhnacademy.account.user.dto.LoginRequestDto;
import com.nhnacademy.account.user.entity.Member;

public interface MemberService {

    Member getMemberIdAndPassword(LoginRequestDto loginRequestDto);

}
