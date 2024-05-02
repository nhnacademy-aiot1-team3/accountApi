package live.databo3.account.organization.service;

import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsRequest;
import live.databo3.account.organization.dto.OrgsRequest;

import java.util.List;

public interface OrganizationService {
    void addOrganization(OrgsRequest request);

    List<GetOrgsResponse> getAllOrganizations();

    void deleteOrganization(Integer organizationId);

    void modifyOrganization(Integer organizationId, ModifyOrgsRequest request);

    GetOrgsResponse getOrganization(Integer organizationId);
}

