package com.nhnacademy.account.user.controller;

import com.nhnacademy.account.user.dto.LoginRequestDto;
import com.nhnacademy.account.user.dto.LoginResponseDto;
import com.nhnacademy.account.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nhnacademy.account.user.service.MemberService;

@RestController
@RequestMapping("/member/login")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<LoginResponseDto> getMemberIdAndPassword(@PathVariable("memberId")String memberId){
        log.info("{}", "here");
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberIdAndPassword(memberId));
    }
}
