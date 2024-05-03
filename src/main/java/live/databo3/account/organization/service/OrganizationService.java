package live.databo3.account.organization.service;

import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsRequest;
import live.databo3.account.organization.dto.OrgsRequest;
import live.databo3.account.organization.dto.PutGatewayOrControllerDto;

import java.util.List;

public interface OrganizationService {
    void addOrganization(OrgsRequest request);

    List<GetOrgsResponse> getAllOrganizations();

    void deleteOrganization(Integer organizationId);

    void modifyOrganization(Integer organizationId, ModifyOrgsRequest request);

    GetOrgsResponse getOrganization(Integer organizationId);

    void putSerialNumber(Integer organizationId, PutGatewayOrControllerDto request);

    void deleteGatewaySn(Integer organizationId);

    void deleteControllerSn(Integer organizationId);
}

