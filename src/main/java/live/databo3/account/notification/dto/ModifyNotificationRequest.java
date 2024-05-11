package live.databo3.account.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyNotificationRequest {
    private String title;
    private String content;
    private String file;
}
