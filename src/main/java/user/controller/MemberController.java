package user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.dto.LoginRequestDto;
import user.entity.Member;
import user.service.MemberService;

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
