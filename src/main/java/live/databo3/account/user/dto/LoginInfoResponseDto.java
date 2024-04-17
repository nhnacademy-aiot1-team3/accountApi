package live.databo3.account.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 데이터베이스에서 id로 조회한 Member 결과를 담을 response Dto
 * @author insub
 * @version 1.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoResponseDto {
    private Long memberNumber;
    private String memberId;
    private String memberPassword;
    private String memberEmail;
    private String role;
    private String state;
    private LocalDateTime lastLoginDateTime;
}
