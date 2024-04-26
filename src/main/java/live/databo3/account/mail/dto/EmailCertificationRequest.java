package live.databo3.account.mail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class EmailCertificationRequest {

    @NotBlank(message = "이메일 입력은 필수 입니다.")
    @Pattern(regexp =  "^[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$",
            message = "이메일 형식이 맞지 않습니다.")
    private String email;
}
