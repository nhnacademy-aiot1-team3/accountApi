package live.databo3.account.memberOrgs.service.impl;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.memberOrgs.dto.GetMembersByStateResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsListResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsWithoutMemberResponse;
import live.databo3.account.memberOrgs.repository.MemberOrgsRepository;
import live.databo3.account.memberOrgs.service.MemberOrgsService;
import live.databo3.account.memberOrgs.entity.MemberOrg;
import live.databo3.account.organization.entity.Organization;
import live.databo3.account.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MemberOrgs Service의 구현체
 * @author jihyeon
 * @version 1.0.1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberOrgsServiceImpl implements MemberOrgsService {
    private final MemberOrgsRepository memberOrgsRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 조직 구성원 중에 특정 멤버가 있는가에 대한 판별
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @return 1-특정 조직에 특정 멤버가 없을 경우, 2-특정 조직에 특정 멤버가 있지만 state가 1일 경우, 3-특정 조직에 특정 멤버가 있지만 state가 2일 경우
     * @since 1.0.0
     */
    @Override
    public Integer booleanMemberOrgs(Integer organizationId, String memberId) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(memberId);
        if(memberOptional.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if(!organizationRepository.existsById(organizationId)) {
            throw new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }
        Optional<MemberOrg> memberOrgsOptional = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organizationId, memberId);
        if(memberOrgsOptional.isEmpty()){
            return 1;
        }
        MemberOrg memberOrg = memberOrgsOptional.get();
        if (memberOrg.getState() == 1) {
            return 2;
        }
        return 3;
    }

    /**
     * 특정 멤버가 소속된 조직 외의 모든 조직 리스트
     * @param memberId 멤버 아이디
     * @return GetOrgsWithoutMemberResponse(organizationId, organizationName)의 리스트
     * @since 1.0.0
     */
    @Override
    public List<GetOrgsWithoutMemberResponse> getOrganizationsWithoutMember(String memberId) {
        memberRepository.findByMemberId(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Organization> allOrganizationList = organizationRepository.findAll();
        List<MemberOrg> memberOrgList = memberOrgsRepository.findByMember_MemberId(memberId);
        List<GetOrgsWithoutMemberResponse> orgList = new ArrayList<>();

        for(Organization organization : allOrganizationList) {
            boolean flag = true;
            for (int i = 0; i < memberOrgList.size(); i++) {
                Integer memberOrgId = memberOrgList.get(i).getOrganization().getOrganizationId();
                if(organization.getOrganizationId().equals(memberOrgId)) {
                    flag = false;
                    break;
                }
            }

            if(flag || memberOrgList.isEmpty()) {
                GetOrgsWithoutMemberResponse getOrgsListResponse = GetOrgsWithoutMemberResponse.builder()
                        .organizationId(organization.getOrganizationId())
                        .organizationName(organization.getOrganizationName())
                        .build();
                orgList.add(getOrgsListResponse);
            }
        }
        return orgList;
    }

    /**
     * 특정 멤버가 속한 조직 리스트
     * @param memberId 멤버 아이디
     * @return GetOrgsListResponse(state, role, organizationId, organizationName)의 리스트
     * @since 1.0.0
     */
    @Override
    public List<GetOrgsListResponse> getOrganizations(String memberId) {
        memberRepository.findByMemberId(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberOrg> organizationList = memberOrgsRepository.findByMember_MemberId(memberId);
        List<GetOrgsListResponse> memberOrgList = new ArrayList<>();
        for(MemberOrg memberOrg : organizationList) {
            log.info("name:{}", memberOrg.getMember().getRoles().getRoleName().name());
            GetOrgsListResponse orgsListResponse = GetOrgsListResponse.builder()
                    .state(memberOrg.getState())
                    .roleName(memberOrg.getMember().getRoles().getRoleName().name())
                    .organizationId(memberOrg.getOrganization().getOrganizationId())
                    .organizationName(memberOrg.getOrganization().getOrganizationName())
                    .build();
            memberOrgList.add(orgsListResponse);
        }
        return memberOrgList;
    }

    /**
     * 특정 조직 구성원에 속한 멤버들의 상태에 따라 가져오기
     * @param organizationId 조직 담당 번호
     * @param state 상태 1(=wait), 2(=approve)
     * @param stringRole role name
     * @return GetMembersByStateResponse(memberId, memberEmail, role, state)의 리스트
     * @since 1.0.0
     */
    @Override
    public List<GetMembersByStateResponse> getMemberListByState(Integer organizationId, Integer state, String stringRole) {
        Roles.ROLES role = null;
        for(Roles.ROLES roles : Roles.ROLES.values()) {
            if(roles.name().equalsIgnoreCase(stringRole)) {
                role = roles;
                break;
            }
        }
        if(role == null) {
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }

        List<MemberOrg> memberOrgList = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_Roles_RoleNameAndState(organizationId, role, state);
        List<GetMembersByStateResponse> memberList = new ArrayList<>();
        for(MemberOrg memberOrg : memberOrgList) {
            GetMembersByStateResponse response = GetMembersByStateResponse.builder()
                    .memberId(memberOrg.getMember().getMemberId())
                    .memberEmail(memberOrg.getMember().getMemberEmail())
                    .roleName(memberOrg.getMember().getRoles().getRoleName().name())
                    .state(memberOrg.getState())
                    .build();

            memberList.add(response);
        }
        return memberList;
    }

    /**
     * 특정 조직 구성원에 특정 멤버가 소속하도록 추가
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @since 1.0.0
     */
    @Override
    public void addMemberOrgs(Integer organizationId, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND));

        Optional<MemberOrg> memberOrgOptional = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organizationId,memberId);
        if(memberOrgOptional.isPresent()) {
            throw new CustomException(ErrorCode.MEMBERORG_ALREADY_EXIST);
        }
        MemberOrg memberOrg = MemberOrg.builder()
                .state(1)
                .member(member)
                .organization(organization)
                .build();
        memberOrgsRepository.save(memberOrg);
    }

    /**
     * 특정 조직 구성원에 속한 특정 멤버에 대한 상태 변경
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @param state 상태 1(=wait), 2(=approve)
     * @since 1.0.0
     */
    @Override
    public void modifyState(Integer organizationId, String memberId, Integer state) {
        memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        organizationRepository.findById(organizationId).orElseThrow(() -> new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND));
        MemberOrg memberOrg = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organizationId, memberId)
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBERORG_NOT_FOUND));

        memberOrg.updateState(state);
        memberOrgsRepository.save(memberOrg);
    }

    /**
     * 특정 멤버를 특정 조직 구성원에서 삭제
     * @param organizationId 조직 담당 번호
     * @param memberId 멤버 아이디
     * @since 1.0.0
     */
    @Override
    public void deleteMemberOrgs(Integer organizationId, String memberId) {
        organizationRepository.findById(organizationId).orElseThrow(() -> new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND));
        memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Optional<MemberOrg> memberOrgOptional = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organizationId, memberId);
        if(memberOrgOptional.isEmpty()){
            throw new CustomException(ErrorCode.MEMBERORG_NOT_FOUND);
        }
        memberOrgsRepository.delete(memberOrgOptional.get());
    }
}
