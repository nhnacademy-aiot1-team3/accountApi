package com.nhnacademy.account.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.nhnacademy.account.user.dto.LoginRequestDto;
import com.nhnacademy.account.user.entity.Member;
import com.nhnacademy.account.user.repository.MemberRepository;
import com.nhnacademy.account.user.service.MemberService;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberIdAndPassword(LoginRequestDto loginRequestDto) {

        return new Member("admin","1234");
    }
}
