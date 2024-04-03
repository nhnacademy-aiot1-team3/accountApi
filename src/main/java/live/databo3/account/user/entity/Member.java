package live.databo3.account.user.entity;

import live.databo3.account.user.dto.JoinResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Member Entity
 * @author insub
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Members")
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;
    @Column(name = "member_password")
    private String pw;
    @Column(name = "member_email")
    private String email;

    /**
     * Member 객체를 생성하는 메서드
     * @param id Member의 id
     * @param pw Member의 password
     * @param email Member의 email
     * @return Member
     */
    public static Member createMember(String id, String pw, String email) {
        return new Member(id, pw, email);
    }

    /**
     * JoinResponseDto로 만들어주는 메소드
     * @return builder로 id, password, email을 반환
     * @since 1.0.0
     */
    public JoinResponseDto toDto() {
        return JoinResponseDto.builder()
                .id(id)
                .password(pw)
                .email(email)
                .build();
    }
}