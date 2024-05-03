package live.databo3.account.organization.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PutGatewayOrControllerDto {
    private String gatewaySn;
    private String controllerSn;
}
