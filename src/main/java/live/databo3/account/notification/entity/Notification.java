package live.databo3.account.notification.entity;

import live.databo3.account.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table (name = "notifications")
public class Notification {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "notification_id")
    private Long notificationId;

    @Column (name = "title")
    private String title;

    @Column (name = "contents")
    private String contents;

    @Column (name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Builder
    public Notification(Long notificationId, String title, String contents, LocalDateTime date, Member member) {
        this.notificationId = notificationId;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.member = member;
    }

    public void change(String title, String contents, Member member){
        this.title = title;
        this.contents = contents;
        this.member = member;
    }
}
