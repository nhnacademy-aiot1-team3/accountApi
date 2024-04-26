package live.databo3.account.mail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class EmailAndCertificationNumberRequest {

    @NotBlank(message = "이메일 입력은 필수 입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$",
            message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "인증번호 입력은 필수 입니다.")
    private String certificationNumber;

}
