package live.databo3.account.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberStateRequestDto {
    private String memberId;
    private Long stateId;
}
