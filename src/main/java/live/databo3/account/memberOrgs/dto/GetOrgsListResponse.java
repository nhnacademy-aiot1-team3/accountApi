package live.databo3.account.memberOrgs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetOrgsListResponse {
    private Integer state;
    private String roleName;
    private Integer organizationId;
    private String organizationName;

    @Builder
    public GetOrgsListResponse(Integer state, Integer organizationId, String organizationName, String roleName) {
        this.state = state;
        this.roleName = roleName;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }
}
