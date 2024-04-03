package com.nhnacademy.account.user.service.impl;


import com.nhnacademy.account.user.dto.JoinRequestDto;
import com.nhnacademy.account.user.dto.JoinResponseDto;
import com.nhnacademy.account.user.dto.LoginInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nhnacademy.account.user.entity.Member;
import com.nhnacademy.account.user.repository.MemberRepository;
import com.nhnacademy.account.user.service.MemberService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginInfoResponseDto getMemberIdAndPassword(String id) {
      
        Member member = memberRepository.findById(id).orElse(null);

        //null 로 변경
        if (Objects.isNull(member)) {
            return null;
        }

        return new LoginInfoResponseDto(member.getId(), member.getPw());
    }

    @Override
    public Boolean isExistByMemberEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public JoinResponseDto createMember(JoinRequestDto request) {
      
        Member member = Member.createMember(request.getId(), passwordEncoder.encode(request.getPassword()), request.getEmail());
      
        if (memberRepository.findById(request.getId()).isPresent()) {
            throw new IllegalStateException("already exist " + member.getId());
        }

        return memberRepository.save(member).toDto();

    }
}
