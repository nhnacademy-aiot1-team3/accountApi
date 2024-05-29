package live.databo3.account.dashboard_config.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterDashboardConfigRequest {
    private Long recordNumber;
    private Long sequenceNumber;
    private String chartType;
}
