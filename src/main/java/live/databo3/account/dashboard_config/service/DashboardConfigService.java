package live.databo3.account.dashboard_config.service;

import live.databo3.account.dashboard_config.dto.DashboardConfigResponse;
import live.databo3.account.dashboard_config.dto.DeleteDashboardConfigRequest;
import live.databo3.account.dashboard_config.dto.ModifyDashboardConfigRequest;
import live.databo3.account.dashboard_config.dto.RegisterDashboardConfigRequest;

import java.util.List;

public interface DashboardConfigService {
    List<DashboardConfigResponse> getDashboardConfig(String memberId);
    void registerDashboardConfig(String memberId, RegisterDashboardConfigRequest requests);
    void modifyDashboardConfig(List<ModifyDashboardConfigRequest> requests);
    void deleteDashboardConfig(Long configId);

    void deleteDashboardConfigs(List<DeleteDashboardConfigRequest> requests);
}
