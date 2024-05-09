package live.databo3.account.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PutGatewayOrControllerDto {
    private String gatewaySn;
    private String controllerSn;

    @Builder
    public PutGatewayOrControllerDto(String gatewaySn, String controllerSn) {
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }
}
