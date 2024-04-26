package live.databo3.account.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *회원가입 응답으로 id, password, email을 전달해주는 DTO
 * @author : 박상진
 * @version : 1.0.0
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinResponseDto {
    private String id;
    private String password;
    private String email;
}
