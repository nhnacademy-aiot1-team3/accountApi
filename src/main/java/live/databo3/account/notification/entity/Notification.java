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
    private String content;

    @Column (name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column (name = "file")
    private String file;

    @Builder
    public Notification(Long notificationId, String title, String content, LocalDateTime date, Member member, String file) {
        this.notificationId = notificationId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.member = member;
        this.file = file;
    }

    public void change(String title, String content, Member member, String file){
        this.title = title;
        this.content = content;
        this.member = member;
        this.file = file;
    }
}
