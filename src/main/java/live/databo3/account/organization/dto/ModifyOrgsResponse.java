package live.databo3.account.organization.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifyOrgsResponse {
    private Integer organizationId;
    private String organizationName;
    private String gatewaySn;
    private String controllerSn;

    @Builder
    public ModifyOrgsResponse(Integer organizationId, String organizationName, String gatewaySn, String controllerSn) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }
}
