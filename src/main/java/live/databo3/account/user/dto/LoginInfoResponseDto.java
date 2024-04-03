package live.databo3.account.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 데이터베이스에서 id로 조회한 Member 결과를 담을 response Dto
 * @author insub
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoResponseDto {
    private String id;
    private String password;

}
