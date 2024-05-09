package live.databo3.account.member.dto;

import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.entity.States;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberDto {
    private Long number;
    private LocalDateTime lastLoginDateTime;
    private String email;
    private String id;
    private Roles.ROLES role;
    private States.STATES state;
}
