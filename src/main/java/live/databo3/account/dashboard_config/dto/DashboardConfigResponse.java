package live.databo3.account.dashboard_config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DashboardConfigResponse {
    Long configId;
    String organization;
    String place;
    String sensorType;
    String sensorSn;
    Long sequenceNumber;
    String chartType;
}
