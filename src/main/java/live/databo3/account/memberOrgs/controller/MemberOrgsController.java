package live.databo3.account.memberOrgs.controller;

import live.databo3.account.memberOrgs.dto.GetMembersByStateResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsListResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsWithoutMemberResponse;
import live.databo3.account.memberOrgs.service.MemberOrgsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 조직 - 멤버 매핑 된 DB 관련 요청을 처리하는 Controller
 * @author jihyeon
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class MemberOrgsController {
    private final MemberOrgsService memberOrgsService;


    /**
     * member가 organization에 속해 있는가 & state가 approve(= 2) 상태인가 확인
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @return ResponseEntity 403 / ResponseEntity 401 / ResponseEntity 200
     * @since 1.0.0
     */
    @GetMapping("/organizations/{organizationId}/members/{memberId}")
    public ResponseEntity<Boolean> booleanMemberOrgs(@PathVariable Integer organizationId, @PathVariable String memberId) {
        Integer result = memberOrgsService.booleanMemberOrgs(organizationId, memberId);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (result == 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * member를 제외한 조직 리스트 가져오기
     * @param memberId 재외하고자하는 memberId
     * @return member가 속한 조직을 제외한 조직 리스트
     * @since 1.0.0
     */
    @GetMapping("/organizations/members-without/{memberId}")
    public ResponseEntity<List<GetOrgsWithoutMemberResponse>> getOrganizationsWithoutMember(@PathVariable String memberId) {
        List<GetOrgsWithoutMemberResponse> organizationList = memberOrgsService.getOrganizationsWithoutMember(memberId);
        return ResponseEntity.ok(organizationList);
    }

    /**
     * 멤버가 해당되는 조직들을 가져오는 method
     * @param memberId 멤버 아이디
     * @return GetOrgsListResponse(organizationId, organizationName, state, role)의 리스트
     * @since 1.0.0
     */
    @GetMapping("/organizations/members/{memberId}")
    public ResponseEntity<List<GetOrgsListResponse>> getOrganizationsByMember(@PathVariable String memberId) {
        List<GetOrgsListResponse> organizationList = memberOrgsService.getOrganizations(memberId);
        return ResponseEntity.ok(organizationList);
    }

    /**
     * state와 role에 따라 멤버 들고 오기
     * @param organizationId 조직 담당 번호
     * @param state MemberOrgs의 state 1(= wait) or 2(= approve)
     * @param role ROLE_ADMIN, ROLE_OWNER, ROLE_VIEWER
     * @return GetMemberByStateResponse(memberId, memberEmail, role, state)의 리스트
     * @since 1.0.0
     */
    @GetMapping("/organizations/{organizationId}/members/state")
    public ResponseEntity<List<GetMembersByStateResponse>> getMemberByState(@PathVariable Integer organizationId, @RequestParam Integer state, @RequestParam String role) {
        List<GetMembersByStateResponse> memberStateWaitDtoList = memberOrgsService.getMemberListByState(organizationId, state, role);
        return ResponseEntity.ok(memberStateWaitDtoList);
    }

    /**
     * 특정 조직에 특정 멤버가 속하도록 저장한다
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @return ResponseEntity 201, message: success
     * @since 1.0.0
     */
    @PostMapping("/organizations/{organizationId}/members/{memberId}")
    public ResponseEntity<HashMap<String, String>> postMemberOrgs(@PathVariable Integer organizationId, @PathVariable String memberId) {
        memberOrgsService.addMemberOrgs(organizationId, memberId);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 특정 조직에 특정 멤버가 속하는 요청 상태를 허용으로 바꿔준다
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @param state 상태 1(= wait) or 2(= approve)
     * @return ResponseEntity 200, message: success
     * @since 1.0.0
     */
    @PutMapping("/organizations/{organizationId}/members/{memberId}")
    public ResponseEntity<HashMap<String, String>> putState(@PathVariable Integer organizationId, @PathVariable String memberId, @RequestParam Integer state) {
        memberOrgsService.modifyState(organizationId, memberId, state);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 조직에 속한 특정 멤버에 대한 매핑을 삭제한다
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @return ResponseEntity 200, message: success
     * @since 1.0.0
     */
    @DeleteMapping("/organizations/{organizationId}/members/{memberId}")
    public ResponseEntity<HashMap<String, String>> deleteMember(@PathVariable Integer organizationId, @PathVariable String memberId) {
        memberOrgsService.deleteMemberOrgs(organizationId, memberId);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }
}
