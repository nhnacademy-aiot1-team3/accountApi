package live.databo3.account.organization.repository;

import live.databo3.account.member.entity.Member;
import live.databo3.account.organization.entity.MemberOrg;
import live.databo3.account.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberOrgsRepository extends JpaRepository<MemberOrg, Long> {
    void deleteAllByOrganization(Organization organization);

    Optional<MemberOrg> findByOrganizationAndMember(Organization organization, Member member);

    List<Organization> findByMember_MemberId(Long memberId);
}
