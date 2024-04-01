package com.nhnacademy.account.user.controller;


import com.nhnacademy.account.user.dto.JoinRequestDto;
import com.nhnacademy.account.user.dto.JoinResponseDto;
import com.nhnacademy.account.user.dto.LoginInfoResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nhnacademy.account.user.service.MemberService;


@Slf4j
@RestController
@RequestMapping("/api/account/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<LoginInfoResponseDto> getMember(@PathVariable("memberId")String memberId){
        log.info("{}", "here");
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberIdAndPassword(memberId));
    }

    @GetMapping("/email/{memberEmail}")
    public ResponseEntity<Boolean> getMemberEmail(@PathVariable("memberEmail")String memberEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.isExistByMemberEmail(memberEmail));
    }


    @PostMapping
    public ResponseEntity<JoinResponseDto> createMember(@RequestBody JoinRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(requestDto));
    }
}
