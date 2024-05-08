package live.databo3.account.memberOrgs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetOrgsWithoutMemberResponse {
    private Integer organizationId;
    private String organizationName;

    @Builder
    public GetOrgsWithoutMemberResponse(Integer organizationId, String organizationName) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }
}
