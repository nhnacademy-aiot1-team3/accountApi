package live.databo3.account.organization.controller;

import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsResponse;
import live.databo3.account.organization.dto.OrgsRequest;
import live.databo3.account.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 조직 관련 요청을 처리하는 Controller
 * @author jihyeon
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class OrganizationController {
    private final OrganizationService organizationService;

    /**
     * 모든 조직들을 가져오는 method
     * @return 모든 조직 리스트
     * @since 1.0.0
     */
    @GetMapping("/organizations")
    public ResponseEntity<List<GetOrgsResponse>> getAllOrganizations() {
        List<GetOrgsResponse> organizationList = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizationList);
    }

    /**
     * 특정 조직을 가져오는 method
     * @param organizationId 가져오길 원하는 조직 id
     * @return GetOrgsResponse(id, name, gateway SerialNumber, controller SerialNumber)
     */
    @GetMapping("/organizations/{organizationId}")
    public ResponseEntity<GetOrgsResponse> getOrganization(@PathVariable Integer organizationId) {
        GetOrgsResponse response = organizationService.getOrganization(organizationId);
        return ResponseEntity.ok(response);
    }

    /**
     * 새로운 조직을 추가하는 method
     * @param request id를 제외한 모든 정보(name, gateway serial number, controller serial number)
     * @return ResponseEntity 204
     * @since 1.0.0
     */
    @PostMapping(value = "/organizations")
    public ResponseEntity<HashMap<String, String>> postOrganization(@RequestBody OrgsRequest request) {
        organizationService.addOrganization(request);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 기존 조직 정보를 수정하는 method
     * @param request 수정할 내용이 담긴 RequestDto
     * @return 수정한 Organization return
     * @since 1.0.0
     */
    @PutMapping("/organizations/{organizationId}")
    public ResponseEntity<ModifyOrgsResponse> putOrganization(@PathVariable("organizationId") Integer organizationId, @RequestBody OrgsRequest request) {
        ModifyOrgsResponse organization = organizationService.modifyOrganization(organizationId, request);
        return ResponseEntity.ok(organization);
    }

    /**
     * 특정 조직 삭제 method
     * @param organizationId 삭제하고픈 조직 id
     * @return ResponseEntity 204
     */
    @DeleteMapping("/organizations/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable("organizationId") Integer organizationId) {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.noContent().build();
    }

    // TODO gateway serialNumber CRUD, controller serialNumber CRUD 만들기

}
