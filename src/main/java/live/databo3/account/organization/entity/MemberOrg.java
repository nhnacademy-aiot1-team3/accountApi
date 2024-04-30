package live.databo3.account.organization.entity;

import live.databo3.account.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class MemberOrg {
    @Id
    @GeneratedValue
    @Column
    private Long recordId;

    @Column
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Builder
    public MemberOrg(Integer state, Member member, Organization organization) {
        this.state = state;
        this.member = member;
        this.organization = organization;
    }
}
