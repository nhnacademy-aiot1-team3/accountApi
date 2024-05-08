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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberOrgsServiceImpl implements MemberOrgsService {
    private final MemberOrgsRepository memberOrgsRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;

    @Override
    public Boolean booleanMemberOrgs(Integer organizationId, String memberId) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(memberId);
        if(memberOptional.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if(!organizationRepository.existsById(organizationId)) {
            throw new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }
        Optional<MemberOrg> memberOrgsOptional = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organizationId, memberId);
        if(memberOrgsOptional.isEmpty()){
            return false;
        }
        MemberOrg memberOrg = memberOrgsOptional.get();
        return memberOrg.getState() == 2;
    }

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

    @Override
    public List<GetOrgsListResponse> getOrganizations(String memberId) {
        memberRepository.findByMemberId(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberOrg> organizationList = memberOrgsRepository.findByMember_MemberId(memberId);
        List<GetOrgsListResponse> memberOrgList = new ArrayList<>();
        for(MemberOrg memberOrg : organizationList) {
            GetOrgsListResponse orgsListResponse = GetOrgsListResponse.builder()
                    .state(memberOrg.getState())
                    .role(memberOrg.getMember().getRoles())
                    .organizationId(memberOrg.getOrganization().getOrganizationId())
                    .organizationName(memberOrg.getOrganization().getOrganizationName())
                    .build();
            memberOrgList.add(orgsListResponse);
        }
        return memberOrgList;
    }

    @Override
    public List<GetMembersByStateResponse> getMemberListByState(Integer organizationId, Integer state, String stringRole) {
        Roles.ROLES role = null;
        for(Roles.ROLES roles : Roles.ROLES.values()) {
            if(roles.name().equals(stringRole)) {
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
                    .role(memberOrg.getMember().getRoles())
                    .state(memberOrg.getState())
                    .build();

            memberList.add(response);
        }
        return memberList;
    }

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

    @Override
    public void modifyState(Integer organizationId, String memberId, Integer state) {
        MemberOrg memberOrg = memberOrgsRepository.findByOrganization_OrganizationIdAndMember_MemberId(organizationId, memberId)
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBERORG_NOT_FOUND));

        memberOrg.setState(state);
        memberOrgsRepository.save(memberOrg);
    }

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
