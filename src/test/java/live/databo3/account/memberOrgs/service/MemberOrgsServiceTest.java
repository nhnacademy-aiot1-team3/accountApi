package live.databo3.account.memberOrgs.service;

import live.databo3.account.exception.CustomException;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.entity.States;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.memberOrgs.dto.GetMembersByStateResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsListResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsWithoutMemberResponse;
import live.databo3.account.memberOrgs.entity.MemberOrg;
import live.databo3.account.memberOrgs.repository.MemberOrgsRepository;
import live.databo3.account.memberOrgs.service.impl.MemberOrgsServiceImpl;
import live.databo3.account.organization.entity.Organization;
import live.databo3.account.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberOrgsServiceTest {
    @Mock
    private MemberOrgsRepository memberOrgsRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private MemberOrgsServiceImpl memberOrgsService;

    @Test
    @DisplayName("특정 조직의 구성원 중 특정 멤버가 있는가에 대한 판별 - 특정 조직에 특정 멤버가 없을 경우")
    void booleanMemberOrgs1() {
        Member member = new Member();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.existsById(any())).willReturn(true);

        Integer result = memberOrgsService.booleanMemberOrgs(1, "testId");

        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("특정 조직의 구성원 중 특정 멤버가 있는가에 대한 판별 - 특정 조직에 특정 멤버가 있지만 state가 1일 경우")
    void booleanMemberOrgs2() {
        Member member = new Member();

        MemberOrg memberOrg = MemberOrg.builder()
                .state(1)
                .member(member).build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.existsById(any())).willReturn(true);
        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(any(), any())).willReturn(Optional.of(memberOrg));

        Integer result = memberOrgsService.booleanMemberOrgs(1, "testId");

        Assertions.assertEquals(2, result);
    }

    @Test
    @DisplayName("특정 조직의 구성원 중 특정 멤버가 있는가에 대한 판별 성공")
    void booleanMemberOrgs3() {
        Member member = new Member();

        MemberOrg memberOrg = MemberOrg.builder()
                .state(2)
                .member(member).build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.existsById(any())).willReturn(true);
        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(any(), any())).willReturn(Optional.of(memberOrg));

        Integer result = memberOrgsService.booleanMemberOrgs(1, "testId");

        Assertions.assertEquals(3, result);
    }

    @Test
    @DisplayName("특정 조직의 구성원 중 특정 멤버가 있는가에 대한 판별 실패 - 없는 멤버")
    void booleanMemberOrgsFail1() {
        try{
            Integer result = memberOrgsService.booleanMemberOrgs(1, "testId");
        } catch (Exception e){
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
            Assertions.assertEquals(CustomException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("특정 조직의 구성원 중 특정 멤버가 있는가에 대한 판별 실패 - 없는 조직")
    void booleanMemberOrgsFail2() {
        Member member = new Member();

        MemberOrg memberOrg = MemberOrg.builder()
                .state(2)
                .member(member).build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.existsById(any())).willReturn(false);

        try{
            Integer result = memberOrgsService.booleanMemberOrgs(1, "testId");
        } catch (Exception e){
            Assertions.assertEquals("조회한 조직이 없습니다", e.getMessage());
            Assertions.assertEquals(CustomException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("특정 멤버가 소속된 조직 외의 모든 조직 리스트 - member가 속한 조직이 있을 경우")
    void getOrganizationWithoutMember() {
        Member member = new Member();
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();

        Organization organization1 = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        Organization organization2 = Organization.builder()
                .organizationId(2)
                .organizationName("nhn 서울")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        List<Organization> organizationList = List.of(organization1, organization2);

        MemberOrg memberOrg = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization).build();
        List<MemberOrg> memberOrgList = List.of(memberOrg);

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.findAll()).willReturn(organizationList);
        given(memberOrgsRepository.findByMember_MemberId(any())).willReturn(memberOrgList);

        List<GetOrgsWithoutMemberResponse> responseList = memberOrgsService.getOrganizationsWithoutMember("testId");

        Assertions.assertEquals(1, responseList.size());
        Assertions.assertEquals(2, responseList.get(0).getOrganizationId());
        Assertions.assertEquals("nhn 서울", responseList.get(0).getOrganizationName());
    }

    @Test
    @DisplayName("특정 멤버가 소속된 조직 외의 모든 조직 리스트 - member가 속한 조직이 없을 경우")
    void getOrganizationWithoutMember2() {
        Member member = new Member();
        Organization organization1 = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        Organization organization2 = Organization.builder()
                .organizationId(2)
                .organizationName("nhn 서울")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        List<Organization> organizationList = List.of(organization1, organization2);

        List<MemberOrg> memberOrgList = List.of();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));

        given(organizationRepository.findAll()).willReturn(organizationList);
        given(memberOrgsRepository.findByMember_MemberId(any())).willReturn(memberOrgList);

        List<GetOrgsWithoutMemberResponse> responseList = memberOrgsService.getOrganizationsWithoutMember("testId");

        Assertions.assertEquals(2, responseList.size());
        Assertions.assertEquals(1, responseList.get(0).getOrganizationId());
        Assertions.assertEquals("nhn 김해", responseList.get(0).getOrganizationName());
        Assertions.assertEquals(2, responseList.get(1).getOrganizationId());
        Assertions.assertEquals("nhn 서울", responseList.get(1).getOrganizationName());
    }

    @Test
    @DisplayName("특정 멤버가 소속된 조직 외의 모든 조직 리스트 - 없는 멤버")
    void getOrganizationWithoutMemberFail() {
        try {
            List<GetOrgsWithoutMemberResponse> responseList = memberOrgsService.getOrganizationsWithoutMember("testId");
        } catch (Exception e){
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
        }
    }

    @Test
    @DisplayName("특정 멤버가 속한 조직 리스트")
    void getOrganizations() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);
        Organization organization1 = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        Organization organization2 = Organization.builder()
                .organizationId(2)
                .organizationName("nhn 서울")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();

        MemberOrg memberOrg1 = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization1)
                .build();
        MemberOrg memberOrg2 = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization2)
                .build();

        List<MemberOrg> memberOrgList = List.of(memberOrg1, memberOrg2);

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(memberOrgsRepository.findByMember_MemberId(any())).willReturn(memberOrgList);

        List<GetOrgsListResponse> responseList = memberOrgsService.getOrganizations("testId");

        Assertions.assertEquals(2, responseList.size());
        Assertions.assertEquals(1, responseList.get(0).getOrganizationId());
        Assertions.assertEquals(2, responseList.get(1).getOrganizationId());
        Assertions.assertEquals("nhn 김해", responseList.get(0).getOrganizationName());
        Assertions.assertEquals("nhn 서울", responseList.get(1).getOrganizationName());
        Assertions.assertEquals(2, responseList.get(0).getState());
        Assertions.assertEquals(2, responseList.get(1).getState());
        Assertions.assertNotNull(responseList.get(0).getRole());
        Assertions.assertNotNull(responseList.get(1).getRole());
        Assertions.assertNotNull(responseList.get(0).getState());
        Assertions.assertNotNull(responseList.get(1).getState());
    }

    @Test
    @DisplayName("특정 멤버가 속한 조직 리스트 - 없는 멤버")
    void getOrganizationsFail() {
        try {
            List<GetOrgsListResponse> responseList = memberOrgsService.getOrganizations("testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
        }
    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 멤버들의 상태에 따라 가져오기")
    void getMemberListByState() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);
        Organization organization1 = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        Organization organization2 = Organization.builder()
                .organizationId(2)
                .organizationName("nhn 서울")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        MemberOrg memberOrg1 = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization1)
                .build();
        MemberOrg memberOrg2 = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization2)
                .build();

        List<MemberOrg> memberOrgList = List.of(memberOrg1, memberOrg2);

        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_Roles_RoleNameAndState(any(), any(), any())).willReturn(memberOrgList);

        List<GetMembersByStateResponse> responseList = memberOrgsService.getMemberListByState(1, 2, "ROLE_OWNER");

        Assertions.assertNotNull(memberOrgsService.getMemberListByState(1,2, "role_owner"));

        Assertions.assertEquals(2, responseList.size());
        Assertions.assertEquals("testId1", responseList.get(0).getMemberId());
        Assertions.assertEquals("testId1", responseList.get(1).getMemberId());
        Assertions.assertEquals("email", responseList.get(0).getMemberEmail());
        Assertions.assertEquals("email", responseList.get(0).getMemberEmail());
        Assertions.assertNotNull(responseList.get(0).getRole());
        Assertions.assertNotNull(responseList.get(1).getRole());
        Assertions.assertNotNull(responseList.get(0).getState());
        Assertions.assertNotNull(responseList.get(1).getState());
    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 멤버들의 상태에 따라 가져오기 실패 - 잘못된 role 명칭")
    void getMemberListByStateFail() {
        try{
            List<GetMembersByStateResponse> responseList = memberOrgsService.getMemberListByState(1, 2, "ROLE_owner");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("역할을 찾을 수 없습니다.", e.getMessage());
        }
    }

    @Test
    @DisplayName("특정 조직 구성원에 특정 멤버가 소속하도록 추가")
    void addMemberOrgs() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));
        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(any(), any())).willReturn(Optional.empty());

        memberOrgsService.addMemberOrgs(1,"testId");

        verify(memberOrgsRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("특정 조직 구성원에 특정 멤버가 소속하도록 추가 실패 - 없는 멤버")
    void addMemberOrgsFail1() {
        try {
            memberOrgsService.addMemberOrgs(1,"testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
        }

        verify(memberOrgsRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("특정 조직 구성원에 특정 멤버가 소속하도록 추가 실패 - 없는 조직")
    void addMemberOrgsFail2() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);
        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        try {
            memberOrgsService.addMemberOrgs(1,"testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 조직이 없습니다", e.getMessage());
        }
        verify(memberOrgsRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("특정 조직 구성원에 특정 멤버가 소속하도록 추가 실패 - 없는 조직")
    void addMemberOrgsFail3() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        MemberOrg memberOrg = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization)
                .build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));
        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(any(), any())).willReturn(Optional.of(memberOrg));

        try {
            memberOrgsService.addMemberOrgs(1,"testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("이미 신청되었습니다", e.getMessage());
        }
        verify(memberOrgsRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 특정 멤버에 대한 상태 변경")
    void modifyState() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        MemberOrg memberOrg = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization)
                .build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));
        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(any(), any())).willReturn(Optional.of(memberOrg));

        memberOrgsService.modifyState(1, "testId", 2);

        verify(memberOrgsRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 특정 멤버에 대한 상태 변경 실패 - 없는 멤버")
    void modifyStateFail1() {
        try{
            memberOrgsService.modifyState(1, "testId", 2);
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
        }
        verify(memberOrgsRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 특정 멤버에 대한 상태 변경 실패 - 없는 멤버")
    void modifyStateFail2() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));

        try{
            memberOrgsService.modifyState(1, "testId", 2);
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 조직이 없습니다", e.getMessage());
        }
        verify(memberOrgsRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 특정 멤버에 대한 상태 변경 실패 - 해당 조직구성원에 멤버가 없음")
    void modifyStateFail3() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId1","pw","email",roles, states);

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));

        try{
            memberOrgsService.modifyState(1, "testId", 2);
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("해당 조직에 멤버가 없습니다", e.getMessage());
        }
        verify(memberOrgsRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("특정 멤버를 특정 조직 구성원에서 삭제")
    void deleteMemberOrgs() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId","pw","email",roles, states);

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        MemberOrg memberOrg = MemberOrg.builder()
                .state(2)
                .member(member)
                .organization(organization)
                .build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));
        given(memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(any(),any())).willReturn(Optional.of(memberOrg));

        memberOrgsService.deleteMemberOrgs(1, "testId");

        verify(memberOrgsRepository,times(1)).delete(any());
    }

    @Test
    @DisplayName("특정 멤버를 특정 조직 구성원에서 삭제 - 없는 조직")
    void deleteMemberOrgsFail1() {
        try{
            memberOrgsService.deleteMemberOrgs(1, "testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 조직이 없습니다", e.getMessage());
        }

        verify(memberOrgsRepository,times(0)).delete(any());
    }

    @Test
    @DisplayName("특정 멤버를 특정 조직 구성원에서 삭제 - 없는 멤버")
    void deleteMemberOrgsFail2() {
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();
        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));

        try{
            memberOrgsService.deleteMemberOrgs(1, "testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
        }

        verify(memberOrgsRepository,times(0)).delete(any());
    }

    @Test
    @DisplayName("특정 멤버를 특정 조직 구성원에서 삭제 - 해당 조직구성원에 멤버가 없음")
    void deleteMemberOrgsFail3() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("testId","pw","email",roles, states);

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gateway")
                .controllerSn("controller")
                .build();

        given(organizationRepository.findById(any())).willReturn(Optional.of(organization));
        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));

        try{
            memberOrgsService.deleteMemberOrgs(1, "testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("해당 조직에 멤버가 없습니다", e.getMessage());
        }

        verify(memberOrgsRepository,times(0)).delete(any());
    }

}
