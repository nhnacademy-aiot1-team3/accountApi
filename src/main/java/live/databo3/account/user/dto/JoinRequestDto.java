package live.databo3.account.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 시 id, password, email을 전달 하는 DTO
 * @author : 박상진
 * @version : 1.0.0
 */
@Getter
@NoArgsConstructor
public class JoinRequestDto {
    private String id;
    private String password;
    private String email;
    private Long roles;
}
