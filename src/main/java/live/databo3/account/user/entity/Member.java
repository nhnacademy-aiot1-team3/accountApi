package live.databo3.account.user.entity;

import live.databo3.account.user.dto.JoinResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Member Entity
 * @author insub
 * @version 1.0.1
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_number")
    private Long memberNumber;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "member_password")
    private String memberPassword;
    @Column(name = "member_email")
    private String memberEmail;
    @Column(name = "last_login_date_time")
    private LocalDateTime lastLoginDateTime;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Roles roles;

    @ManyToOne
    @JoinColumn(name="state_id")
    private States states;

    /**
     * Member 객체를 생성하는 메서드
     * @param id Member의 id
     * @param pw Member의 password
     * @param email Member의 email
     * @return Member
     */
    public static Member createMember(String id, String pw, String email, Roles roles, States states) {
        return new Member(null,id, pw, email, null, roles, states);
    }

    /**
     * JoinResponseDto로 만들어주는 메소드
     * @return builder로 id, password, email을 반환
     * @since 1.0.0
     */
    public JoinResponseDto toDto() {
        return JoinResponseDto.builder()
                .id(memberId)
                .password(memberPassword)
                .email(memberEmail)
                .build();
    }
}