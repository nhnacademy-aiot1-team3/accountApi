package live.databo3.account.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMemberResponseDto {
    private String id;
    private String password;
    private String email;
    private String role;
}
