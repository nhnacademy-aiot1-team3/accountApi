package live.databo3.account.organization.service;

import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsResponse;
import live.databo3.account.organization.dto.OrgsRequest;

import java.util.List;

public interface OrganizationService {
    void addOrganization(OrgsRequest request);

    List<GetOrgsResponse> getAllOrganizations();

    void deleteOrganization(Integer organizationId);

    ModifyOrgsResponse modifyOrganization(Integer organizationId, OrgsRequest request);

    GetOrgsResponse getOrganization(Integer organizationId);
}

