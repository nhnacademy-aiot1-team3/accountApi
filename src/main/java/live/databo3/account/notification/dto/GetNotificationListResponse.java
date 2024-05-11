package live.databo3.account.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetNotificationListResponse {
    private Long notificationId;
    private String title;
    private String author;
    private LocalDateTime date;

    @Builder
    public GetNotificationListResponse(Long notificationId, String title, String author, LocalDateTime date) {
        this.notificationId = notificationId;
        this.title = title;
        this.author = author;
        this.date = date;
    }
}
