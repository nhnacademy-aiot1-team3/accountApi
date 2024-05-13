package live.databo3.account.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyNotificationRequest {
    private String title;
    private String contents;
    private String file;

    @Builder
    public ModifyNotificationRequest(String title, String contents, String file) {
        this.title = title;
        this.contents = contents;
        this.file = file;
    }
}
