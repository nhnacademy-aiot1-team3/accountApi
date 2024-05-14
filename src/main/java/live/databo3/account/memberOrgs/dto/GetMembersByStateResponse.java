package live.databo3.account.memberOrgs.dto;

import live.databo3.account.member.entity.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMembersByStateResponse {
    private String memberId;
    private String memberEmail;
    private String roleName;
    private Integer state;

    @Builder
    public GetMembersByStateResponse(String memberId, String memberEmail, String roleName, Integer state) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.roleName = roleName;
        this.state = state;
    }
}
