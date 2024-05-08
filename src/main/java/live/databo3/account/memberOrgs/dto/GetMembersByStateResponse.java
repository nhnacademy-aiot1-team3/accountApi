package live.databo3.account.memberOrgs.dto;

import live.databo3.account.member.entity.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMembersByStateResponse {
    private Long memberNumber;
    private String memberEmail;
    private Roles role;
    private Integer state;

    @Builder
    public GetMembersByStateResponse(Long memberNumber, String memberEmail, Roles role, Integer state) {
        this.memberNumber = memberNumber;
        this.memberEmail = memberEmail;
        this.role = role;
        this.state = state;
    }
}
