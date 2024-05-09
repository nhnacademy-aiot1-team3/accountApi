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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class MemberOrgsController {
    private final MemberOrgsService memberOrgsService;

    // TODO user가  organization에 속해있는가 & state가 approve 상태인가 확인
    //TODO GET 멤버, 조직 조회로 현 상태 들고 오기
    @GetMapping("/organizations/{organizationId}/members/{memberId}")
    public ResponseEntity<Boolean> booleanMemberOrgs(@PathVariable Integer organizationId, @PathVariable String memberId) {
        Boolean response = memberOrgsService.booleanMemberOrgs(organizationId, memberId);
        return ResponseEntity.ok(response);
    }

    // TODO GET 본인이 속한 조직을 제외한 조직 다 들고오기 (상태 상관 X)
    /**
     * member를 제외한 조직 리스트 가져오기
     * @param memberId 재외하고자하는 memberId
     * @return member가 속한 조직을 제외한 조직 리스트
     * @since 1.0.0
     */
    @GetMapping("/organizations/members-without")
    public ResponseEntity<List<GetOrgsWithoutMemberResponse>> getOrganizationsWithoutMember(@RequestHeader("X-USER-ID") String memberId) {
        List<GetOrgsWithoutMemberResponse> organizationList = memberOrgsService.getOrganizationsWithoutMember(memberId);
        return ResponseEntity.ok(organizationList);
    }

    // TODO GET 멤버를 기반으로 속한 조직들 다 들고 오기(상태 X) // organizationId, organizationName, state, role

    /**
     * 멤버가 해당되는 조직들을 가져오는 method
     * @param memberId 멤버 번호
     * @return 멤버가 속한 조직 리스트
     * @since 1.0.0
     */
    @GetMapping("/organizations/members")
    public ResponseEntity<List<GetOrgsListResponse>> getOrganizationsByMember(@RequestHeader("X-USER-ID") String memberId) {
        List<GetOrgsListResponse> organizationList = memberOrgsService.getOrganizations(memberId);
        return ResponseEntity.ok(organizationList);
    }

    // TODO GET state와 role에 따른 멤버 들고오기 //id, email, role, state

    @GetMapping("/organizations/{organizationId}/members/state")
    public ResponseEntity<List<GetMembersByStateResponse>> getMemberByState(@PathVariable Integer organizationId, @RequestParam Integer state, @RequestParam String role) {
        List<GetMembersByStateResponse> memberStateWaitDtoList = memberOrgsService.getMemberListByState(organizationId, state, role);
        return ResponseEntity.ok(memberStateWaitDtoList);
    }

    // TODO POST
    @PostMapping("/organizations/{organizationId}/members")
    public ResponseEntity<HashMap<String, String>> postMemberOrgs(@PathVariable Integer organizationId, @RequestHeader("X-USER-ID") String memberId) {
        memberOrgsService.addMemberOrgs(organizationId, memberId);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //TODO PUT state 바꿈
    @PutMapping("/organizations/{organizationId}/members")
    public ResponseEntity<HashMap<String, String>> putState(@PathVariable Integer organizationId, @RequestHeader("X-USER-ID") String memberId, @RequestParam Integer state) {
        memberOrgsService.modifyState(organizationId, memberId, state);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    // TODO DELETE
    @DeleteMapping("/organizations/{organizationId}/members")
    public ResponseEntity<HashMap<String, String>> deleteMember(@PathVariable Integer organizationId, @RequestHeader("X-USER-ID") String memberId) {
        memberOrgsService.deleteMemberOrgs(organizationId, memberId);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }
}
