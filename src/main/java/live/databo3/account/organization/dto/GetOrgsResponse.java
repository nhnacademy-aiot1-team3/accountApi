package live.databo3.account.organization.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetOrgsResponse {
    private Integer organizationId;
    private String organizationName;
    private String gatewaySn;
    private String controllerSn;

    @Builder
    public GetOrgsResponse(Integer organizationId, String organizationName, String gatewaySn, String controllerSn) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.gatewaySn = gatewaySn;
        this.controllerSn = controllerSn;
    }
}
