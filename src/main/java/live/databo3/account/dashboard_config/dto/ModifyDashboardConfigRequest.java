package live.databo3.account.dashboard_config.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyDashboardConfigRequest {
    Long configId;
    Long sequenceNumber;
}
