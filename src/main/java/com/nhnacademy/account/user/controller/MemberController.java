package com.nhnacademy.account.user.controller;

import com.nhnacademy.account.user.dto.LoginRequestDto;
import com.nhnacademy.account.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nhnacademy.account.user.service.MemberService;

@RestController
@RequestMapping("/member/login")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Member> getMemberIdAndPassword(LoginRequestDto loginRequestDto){

        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberIdAndPassword(loginRequestDto));
    }
}
