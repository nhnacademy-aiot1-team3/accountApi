package live.databo3.account.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private int code;
    private String message;
    private LocalDateTime localDateTime;
}
