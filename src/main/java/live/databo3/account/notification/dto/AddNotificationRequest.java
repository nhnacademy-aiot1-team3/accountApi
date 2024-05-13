package live.databo3.account.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddNotificationRequest {
    private String title;
    private String contents;
    private String file;
}
