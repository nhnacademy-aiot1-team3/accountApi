package live.databo3.account.memberOrgs.service;

import live.databo3.account.memberOrgs.dto.GetMembersByStateResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsListResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsWithoutMemberResponse;

import java.util.List;

public interface MemberOrgsService {

    Integer booleanMemberOrgs(Integer organizationId, String memberId);

    List<GetOrgsWithoutMemberResponse> getOrganizationsWithoutMember(String memberId);

    List<GetOrgsListResponse> getOrganizations(String memberId);

    List<GetMembersByStateResponse> getMemberListByState(Integer organizationId, Integer state, String role);

    void addMemberOrgs(Integer organizationId, String memberId);

    void modifyState(Integer organizationId, String memberId, Integer state);

    void deleteMemberOrgs(Integer organizationId, String memberId);
}
