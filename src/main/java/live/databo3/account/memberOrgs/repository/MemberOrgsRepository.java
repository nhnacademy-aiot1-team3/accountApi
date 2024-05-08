package live.databo3.account.memberOrgs.repository;

import live.databo3.account.member.entity.Roles;
import live.databo3.account.memberOrgs.entity.MemberOrg;
import live.databo3.account.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberOrgsRepository extends JpaRepository<MemberOrg, Long> {
    /**
     * 조직에 해당하는 모든 MemberOrgs 삭제
     * @param organization 조직
     * @since 1.0.0
     */
    void deleteAllByOrganization(Organization organization);

    /**
     * organizationId, memberNumber으로 MemberOrg 찾기
     * @param organizationId 조직 넘버
     * @param memberId 멤버 아이디
     * @return Optional<MemberOrg>
     * @since 1.0.0
     */
    Optional<MemberOrg> findByOrganization_OrganizationIdAndMember_MemberId(Integer organizationId, String memberId);

    /**
     * organizationId와 role, state에 맞는 MemberOrgs들 찾기
     * @param organizationId 조직 넘버
     * @param role ROLE_ADMIN, ROLE_OWNER, ROLE_VIEWER 중 하나
     * @param state 상태 1 = wait, 2 = approve
     * @return List<MemberOrg>
     * @since 1.0.0
     */
    List<MemberOrg> findByOrganization_OrganizationIdAndMember_Roles_RoleNameAndState(Integer organizationId, Roles.ROLES role, Integer state);

    /**
     * 멤버 아이디로 MeberOrg 조회하기
     * @param memberId 멤버 아이디
     * @return List<MemberOrg>
     * @since 1.0.0
     */
    List<MemberOrg> findByMember_MemberId(String memberId);
}
