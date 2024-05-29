package live.databo3.account.dashboard_config.controller;

import live.databo3.account.dashboard_config.dto.DashboardConfigResponse;
import live.databo3.account.dashboard_config.dto.ModifyDashboardConfigRequest;
import live.databo3.account.dashboard_config.dto.RegisterDashboardConfigRequest;
import live.databo3.account.dashboard_config.service.DashboardConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/account/config/dashboard")
@RequiredArgsConstructor
public class DashboardConfigController {
    private final DashboardConfigService dashboardConfigService;

    @GetMapping
    public ResponseEntity<List<DashboardConfigResponse>> getDashboardConfig(@RequestHeader("X-USER-ID") String memberId) {
        return ResponseEntity.ok(dashboardConfigService.getDashboardConfig(memberId));
    }

    @PostMapping
    public ResponseEntity<HashMap<String, String>> createDashboardConfig(@RequestHeader("X-USER-ID") String memberId, @RequestBody RegisterDashboardConfigRequest request) {
        dashboardConfigService.registerDashboardConfig(memberId, request);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<HashMap<String, String>> modifyDashboardConfig(@RequestBody List<ModifyDashboardConfigRequest> requests) {
        dashboardConfigService.modifyDashboardConfig(requests);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{configId}")
    public ResponseEntity<Void> deleteDashboardConfig(@PathVariable Long configId) {
        dashboardConfigService.deleteDashboardConfig(configId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
