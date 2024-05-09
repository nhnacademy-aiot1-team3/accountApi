package live.databo3.account.memberOrgs.dto;

import live.databo3.account.member.entity.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetOrgsListResponse {
    private Integer state;
    private Roles role;
    private Integer organizationId;
    private String organizationName;

    @Builder
    public GetOrgsListResponse(Integer state, Integer organizationId, String organizationName, Roles role) {
        this.state = state;
        this.role = role;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }
}
