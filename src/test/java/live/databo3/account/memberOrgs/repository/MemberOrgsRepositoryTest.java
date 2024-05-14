package live.databo3.account.memberOrgs.repository;

import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.entity.States;
import live.databo3.account.memberOrgs.entity.MemberOrg;
import live.databo3.account.organization.entity.Organization;
import live.databo3.account.organization.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberOrgsRepositoryTest {
    @Autowired
    private MemberOrgsRepository memberOrgsRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EntityManager entityManager;

    private Member member;
    private Organization organization;

    @BeforeEach
    void setUp() {
        Roles roles = new Roles();
        roles.setRoleName(Roles.ROLES.ROLE_OWNER);
        entityManager.persist(roles);

        States states = new States();
        states.setStateName(States.STATES.ACTIVE);
        entityManager.persist(states);
        log.info("statesId:{}", states.getStateId());

        member = Member.createMember("testId", "pw", "email", roles, states);
        entityManager.persist(member);

        organization = Organization.builder()
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        entityManager.persist(organization);

        MemberOrg memberOrg = MemberOrg.builder()
                .member(member)
                .organization(organization)
                .state(2)
                .build();

        entityManager.persist(memberOrg);
    }

    @Order(1)
    @Test
    @DisplayName("조직에 해당하는 모든 MemberOrgs 삭제")
    void deleteAllByOrganization() {
        memberOrgsRepository.deleteAllByOrganization(organization);
        List<MemberOrg> memberOrgList = memberOrgsRepository.findAll();
        Assertions.assertEquals(0, memberOrgList.size());
    }

    @Order(2)
    @Test
    @DisplayName("organizationId, memberNumber으로 MemberOrg 찾기")
    void findByOrganizationIdAndMemberId() {
        Organization organization1 = organizationRepository.findByOrganizationName("nhn 김해").get();
        log.info("organizationId: {}", organization1.getOrganizationId());
        Optional<MemberOrg> memberOrgOptional = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organization1.getOrganizationId(),"testId");

        Assertions.assertTrue(memberOrgOptional.isPresent());
        Assertions.assertEquals(2, memberOrgOptional.get().getState());
        Assertions.assertEquals(member, memberOrgOptional.get().getMember());
        Assertions.assertEquals(organization, memberOrgOptional.get().getOrganization());
    }

    @Order(3)
    @Test
    @DisplayName("organizationId와 role, state에 맞는 MemberOrgs들 찾기")
    void findByOrganizationIdAndRoleNameAndState() {
        Organization organization1 = organizationRepository.findByOrganizationName("nhn 김해").get();
        log.info("organizationId: {}", organization1.getOrganizationId());
        List<MemberOrg> memberOrgList = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_Roles_RoleNameAndState(organization1.getOrganizationId(), Roles.ROLES.ROLE_OWNER, 2);

        Assertions.assertEquals(1, memberOrgList.size());
        Assertions.assertEquals(2, memberOrgList.get(0).getState());
        Assertions.assertEquals(member, memberOrgList.get(0).getMember());
        Assertions.assertEquals(organization, memberOrgList.get(0).getOrganization());
    }

    @Order(4)
    @Test
    @DisplayName("멤버 아이디로 MeberOrg 조회하기")
    void findByMemberId() {
        List<MemberOrg> memberOrgList = memberOrgsRepository.findByMember_MemberId("testId");

        Assertions.assertEquals(1, memberOrgList.size());
        Assertions.assertEquals(2, memberOrgList.get(0).getState());
        Assertions.assertEquals(member, memberOrgList.get(0).getMember());
        Assertions.assertEquals(organization, memberOrgList.get(0).getOrganization());
    }
}
