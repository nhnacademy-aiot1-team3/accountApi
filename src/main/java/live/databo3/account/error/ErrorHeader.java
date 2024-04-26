package live.databo3.account.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorHeader {
    private int resultCode;
    private String resultMessage;
    private LocalDateTime localDateTime;
}
