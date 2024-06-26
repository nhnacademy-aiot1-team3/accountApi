package live.databo3.account.member.controller;


import io.lettuce.core.dynamic.annotation.Param;
import live.databo3.account.member.dto.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import live.databo3.account.member.service.MemberService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * account-api로 들어오는 Member 관련 요청들을 처리하는 Controller
 *
 * @author insub, 양현성
 * @version 1.0.1
 */
@Slf4j
@RestController
@RequestMapping("/api/account/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * Query String 으로 전달 받은 id로 데이터베이스에서 조회한 결과를 반환하는 메서드
     *
     * @param memberId 조회 하고 싶은 Member 아이디
     * @return 파라미터로 조회한 결과를 반환
     * @since 1.0.0
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<LoginInfoResponseDto> getMember(@PathVariable("memberId") String memberId) {
        log.info("멤버 조회 : {}", memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberIdAndPassword(memberId));
    }

    /**
     * Query String 으로 전달 받은 email로 데이터베이스에서 조회한 결과를 반환하는 메서드
     *
     * @param memberEmail 조회 하고 싶은 Member 이메일
     * @return 파라미터로 조회한 결과를 반환, Boolean type
     * @since 1.0.0
     */
    @GetMapping("/email/{memberEmail}")
    public ResponseEntity<Boolean> getMemberEmail(@PathVariable("memberEmail") String memberEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.isExistByMemberEmail(memberEmail));
    }

    /**
     * Member를 생성하는 메소드
     *
     * @param requestDto ResponseEntity를 구성하는데 필요한 requestDto 파라미터
     * @return ResponseEntity 생성 성공한 Member dto
     * @since 1.0.0
     */
    @PostMapping("/register")
    public ResponseEntity<JoinResponseDto> createMember(@RequestBody JoinRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerMember(requestDto));
    }

    /**
     * Member Roles 변경을 요청하는 메소드
     * 현재 viewer 에서 owner 변경 요청만 가능함
     * 요청 보낼 때 Content-Type: application/x-www-form-urlencoded
     * (version 1.0.1)
     *
     * @param memberId Roles 변경을 원하는 멤버의 아이디
     * @return ResponseEntity 200 ok, message = success
     * @since 1.0.1
     */
    @PutMapping("/upgrade")
    public ResponseEntity<HashMap<String, String>> upgradeRole(@RequestHeader("X-USER-ID") String memberId, @RequestParam("roleId") Long roleId) {
        log.info("권한 승급 아이디 : {}, roleId : {}", memberId, roleId);
        memberService.upgradeRoles(memberId, roleId);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * Member States 변경을 요청하는 메서드
     * 현재 pause 에서 active 로 변경하는 메서드
     *
     * @param request 상태 변경할 멤버의 id, email
     * @return ResponseEntity 200 ok, message = success
     */
    @PutMapping("/state")
    public ResponseEntity<HashMap<String, String>> updateState(@Valid @RequestBody UpdateStateRequest request) {
        log.info("휴면 상태 해제 아이디 : {}", request.getId());
        log.info("휴면 상태 해제 이메일 : {}", request.getEmail());

        //service 추가
        memberService.changeState(request.getId());

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * Member를 수정하는 메소드
     *
     * @param memberId modifyMember()에서 조회하기 위한 파라미터
     * @param request  ResponseEntity를 구성하는데 필요한 requestDto 파라미터
     * @return ResponseEntity 수성한 것을 반환
     * @since 1.0.1
     */
    @PutMapping("/modify/{memberId}")
    public ResponseEntity<UpdateMemberResponseDto> updateMember(@PathVariable("memberId") String memberId, @RequestBody UpdateMemberRequestDto request) {
        return ResponseEntity.ok(memberService.modifyMember(memberId, request));
    }

    /**
     * Member를 탈퇴하는 메소드
     *
     * @param memberId 요청 헤더에 있는 X_USER_ID의 값
     * @return ResponseEntity HTTP 204
     * @since 1.0.1
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMember(@RequestHeader("X-USER-ID") String memberId) {
        log.info("회원 탈퇴 아이디 : {}", memberId);
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 아이디 중복 체크 메서드
     *
     * @param requestId 중복 확인할 아이디
     * @return 200 OK, 중복되면 true, 아니면 false
     */
    @GetMapping("/duplicate/{memberId}")
    public ResponseEntity<Boolean> requestIdDuplicateCheck(@PathVariable("memberId") String requestId) {
        log.info("중복 아이디 체크 : {}", requestId);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.requestIdDuplicateCheck(requestId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> getMembers(@RequestParam(value = "roleId",required = false) Long roleId, @RequestParam(value = "stateId",required = false) Long stateId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getMemberList(roleId, stateId));
    }

    @PutMapping("/modify/state")
    public ResponseEntity<Void> modifyMemberState(@RequestBody UpdateMemberStateRequestDto requestDto) {
        memberService.modifyMemberState(requestDto.getMemberId(), requestDto.getStateId());
        return ResponseEntity.ok().build();
    }
}
