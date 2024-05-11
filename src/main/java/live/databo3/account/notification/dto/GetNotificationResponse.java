package live.databo3.account.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetNotificationResponse {
    private Long notificationId;
    private String title;
    private String content;
    private LocalDateTime date;
    private String memberId;
    private String file;

    @Builder
    public GetNotificationResponse(Long notificationId, String title, String content, LocalDateTime date, String memberId, String file) {
        this.notificationId = notificationId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.memberId = memberId;
        this.file = file;
    }
}
