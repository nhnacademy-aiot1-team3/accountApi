package live.databo3.account.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Member의 내용을 수정할 것을 전달하는 DTO
 * @author 박상진
 * @version 1.0.0
 */
@Getter
@NoArgsConstructor
public class UpdateMemberRequestDto {
    private String id;
    private String password;
    private String email;
    private String role;

}
