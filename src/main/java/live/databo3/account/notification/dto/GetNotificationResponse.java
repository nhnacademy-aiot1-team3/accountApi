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
    private String contents;
    private LocalDateTime date;
    private String memberId;

    @Builder
    public GetNotificationResponse(Long notificationId, String title, String contents, LocalDateTime date, String memberId) {
        this.notificationId = notificationId;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.memberId = memberId;
    }
}
