package live.databo3.account.memberOrgs.entity;

import live.databo3.account.member.entity.Member;
import live.databo3.account.organization.entity.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_orgs")
public class MemberOrg {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "record_number")
    private Long recordNumber;

    @Column (name = "state")
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

    public void updateState(Integer state) {
        this.state = state;
    }
}
