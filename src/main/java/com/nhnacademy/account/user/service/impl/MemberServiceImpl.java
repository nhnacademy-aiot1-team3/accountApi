package com.nhnacademy.account.user.service.impl;

import com.nhnacademy.account.user.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.nhnacademy.account.user.entity.Member;
import com.nhnacademy.account.user.repository.MemberRepository;
import com.nhnacademy.account.user.service.MemberService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public LoginResponseDto getMemberIdAndPassword(String id) {
        Member member = memberRepository.findById(id).orElse(null);

        if(Objects.isNull(member)){
            return new LoginResponseDto("", "");
        }

        return new LoginResponseDto(member.getId(), member.getPw());
    }
}
