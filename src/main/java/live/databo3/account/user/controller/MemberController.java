package live.databo3.account.user.controller;


import live.databo3.account.user.dto.JoinRequestDto;
import live.databo3.account.user.dto.JoinResponseDto;
import live.databo3.account.user.dto.LoginInfoResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import live.databo3.account.user.service.MemberService;

/**
 * account-api로 들어오는 Member 관련 요청들을 처리하는 Controller
 * @author insub
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/account/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * Query String 으로 전달 받은 id로 데이터베이스에서 조회한 결과를 반환하는 메서드
     * @param memberId 조회 하고 싶은 Member 아이디
     * @return 파라미터로 조회한 결과를 반환
     * @since 1.0.0
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<LoginInfoResponseDto> getMember(@PathVariable("memberId")String memberId){
        log.info("멤버 조회 : {}", memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberIdAndPassword(memberId));
    }

    /**
     * Query String 으로 전달 받은 email로 데이터베이스에서 조회한 결과를 반환하는 메서드
     * @param memberEmail 조회 하고 싶은 Member 이메일
     * @return 파라미터로 조회한 결과를 반환, Boolean type
     * @since 1.0.0
     */
    @GetMapping("/email/{memberEmail}")
    public ResponseEntity<Boolean> getMemberEmail(@PathVariable("memberEmail")String memberEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.isExistByMemberEmail(memberEmail));
    }

    /**
     * Member를 create하는 메소드
     * @param requestDto ResponseEntity를 구성하는데 필요한 requestDto 파라미터
     * @return ResponseEntity 생성 성공한 Member dto
     * @since 1.0.0
     */
    @PostMapping("/register")
    public ResponseEntity<JoinResponseDto> createMember(@RequestBody JoinRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerMember(requestDto));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMember(@RequestHeader("X_USER_ID") String userId){
        return null;
    }
}
