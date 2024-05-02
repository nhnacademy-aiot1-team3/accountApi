package live.databo3.account.organization.service.impl;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsRequest;
import live.databo3.account.organization.dto.OrgsRequest;
import live.databo3.account.organization.entity.Organization;
import live.databo3.account.organization.repository.MemberOrgsRepository;
import live.databo3.account.organization.repository.OrganizationRepository;
import live.databo3.account.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OrganizationService의 구현체
 * @author jihyeon
 * @version 1.0.1
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final MemberOrgsRepository memberOrgsRepository;

    /**
     * organization db에 만일 이미 있을 경우 ORGANIZATION_ALREADY_EXIST exception 던짐
     * 만일 없다면 organization을 저장한다
     * @param request OrgsRequest (name, gatewaySn, controllerSn)
     * @since 1.0.0
     */
    @Override
    public void addOrganization(OrgsRequest request) {
        Optional<Organization> org = organizationRepository.findByOrganizationName(request.getOrganizationName());
        if(org.isPresent()) {
            throw new CustomException(ErrorCode.ORGANIZATION_ALREADY_EXIST);
        }
        Organization organization = Organization.builder()
                .organizationName(request.getOrganizationName())
                .gatewaySn(request.getGatewaySn())
                .controllerSn(request.getControllerSn())
                .build();
        organizationRepository.save(organization);
    }

    /**
     * 모든 조직을 전부 조회
     * @return GetOrgResponse(id, name, gateway SerialNumber, controller SerialNumber)
     * @since 1.0.0
     */
    @Override
    public List<GetOrgsResponse> getAllOrganizations() {
        List<Organization> organizationList = organizationRepository.findAll();
        List<GetOrgsResponse> organizationDtoList = new ArrayList<>();
        if(organizationList.isEmpty()) {
            return List.of();
        }
        for(Organization organization : organizationList) {
            GetOrgsResponse response = GetOrgsResponse.builder()
                    .organizationId(organization.getOrganizationId())
                    .organizationName(organization.getOrganizationName())
                    .gatewaySn(organization.getGatewaySn())
                    .controllerSn(organization.getControllerSn())
                    .build();
            organizationDtoList.add(response);
        }
        return organizationDtoList;
    }


    /**
     * 특정 조직을 조회
     * @param organizationId 조회하고자 하는 조직
     * @return GetOrgResponse(id, name, gateway SerialNumber, controller SerialNumber)
     */
    @Override
    public GetOrgsResponse getOrganization(Integer organizationId) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
        if(organizationOptional.isEmpty()) {
            throw new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }
        Organization organization = organizationOptional.get();
        return GetOrgsResponse.builder()
                .organizationId(organization.getOrganizationId())
                .organizationName(organization.getOrganizationName())
                .gatewaySn(organization.getGatewaySn())
                .controllerSn(organization.getControllerSn())
                .build();
    }

    /**
     * organization이 db에 없을 경우 exception (ORGANIZATION_NOT_FOUND)띄움
     * 있을 경우 조직 이름을 변경된 값으로 바꾼 후 저장
     * @param request ModifyOrgsRequest (OrganizationName)
     * @since 1.0.0
     */
    @Override
    public void modifyOrganization(Integer organizationId, ModifyOrgsRequest request) {
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
        if(organizationOptional.isEmpty()) {
            throw new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }
        Organization organization = organizationOptional.get();
        String organizationName = request.getOrganizationName();

        organization.setOrganizationName(organizationName);
        organizationRepository.save(organization);

    }

    /**
     * 특정 조직을 삭제, 조직에 속한 member들 관계 리스트 삭제
     * @param organizationId 삭제하고자 하는 조직 id
     * @since 1.0.0
     */
    @Override
    public void deleteOrganization(Integer organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new CustomException(ErrorCode.ORGANIZATION_NOT_FOUND));

        memberOrgsRepository.deleteAllByOrganization(organization);
        organizationRepository.delete(organization);
    }

}
