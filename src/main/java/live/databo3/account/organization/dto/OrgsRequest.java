package live.databo3.account.organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrgsRequest {
    @Setter
    private String organizationName;
    private String gatewaySn;
    private String controllerSn;

    @Builder
    public OrgsRequest(String organizationName, String gatewaySn, String controllerSn) {
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }
}
