package live.databo3.account.organization.service;

import live.databo3.account.exception.CustomException;
import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsRequest;
import live.databo3.account.organization.dto.OrgsRequest;
import live.databo3.account.organization.dto.PutGatewayOrControllerDto;
import live.databo3.account.organization.entity.Organization;
import live.databo3.account.organization.repository.MemberOrgsRepository;
import live.databo3.account.organization.repository.OrganizationRepository;
import live.databo3.account.organization.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {
    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Test
    @DisplayName("모든 조직 조회 - 한개 이상 있을 경우")
    void getOrganizations() {
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn").build();
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        when(organizationRepository.findAll()).thenReturn(organizationList);

        List<GetOrgsResponse> orgsResponseList = organizationService.getAllOrganizations();

        Assertions.assertNotNull(orgsResponseList);
    }

    @Test
    @DisplayName("모든 조직 조회 - 0개일 경우")
    void getOrganizationsEmpty() {
        List<Organization> organizationList = new ArrayList<>();

        when(organizationRepository.findAll()).thenReturn(organizationList);

        List<GetOrgsResponse> orgsResponseList = organizationService.getAllOrganizations();
        Assertions.assertEquals(orgsResponseList.size(), 0);
    }

    @Test
    @DisplayName("특정 조직 조회 성공")
    void getOrganization() {
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("dddd")
                .controllerSn("dddddddd").build();

        Optional<Organization> organizationOptional = Optional.of(organization);

        when(organizationRepository.findById(any())).thenReturn(organizationOptional);

        GetOrgsResponse getOrgsResponse = organizationService.getOrganization(organization.getOrganizationId());

        Assertions.assertEquals(getOrgsResponse.getOrganizationId(), 1);
        Assertions.assertEquals(getOrgsResponse.getOrganizationName(), "nhn 김해");
        Assertions.assertEquals(getOrgsResponse.getGatewaySn(), "dddd");
        Assertions.assertEquals(getOrgsResponse.getControllerSn(), "dddddddd");
    }

    @Test
    @DisplayName("특정 조직 조회 실패")
    void getOrganizationNotFound() {
        try{
            organizationService.getOrganization(9999);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "조회한 조직이 없습니다");
            Assertions.assertEquals(e.getClass(), CustomException.class);
        }
    }

    @Test
    @DisplayName("조직 추가 성공")
    void postOrgsSuccess(){
        OrgsRequest orgsRequest = OrgsRequest.builder().build();

        Optional<Organization> organizationOptional = Optional.empty();
        when(organizationRepository.findByOrganizationName(any())).thenReturn(organizationOptional);

        organizationService.addOrganization(orgsRequest);

        verify(organizationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("조직 추가 실패 - 이미 있는 이름")
    void postOrgsFail(){
        OrgsRequest orgsRequest = OrgsRequest.builder().build();

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("ddd")
                .controllerSn("eeee")
                .build();

        when(organizationRepository.findByOrganizationName(any())).thenReturn(Optional.of(organization));

        Assertions.assertThrows(CustomException.class, ()->organizationService.addOrganization(orgsRequest));

        verify(organizationRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("조직 이름 수정 성공")
    void modifyOrganizationSuccess(){
        ModifyOrgsRequest request = ModifyOrgsRequest.builder()
                .organizationName("nhn 김해").build();

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));
        when(organizationRepository.findByOrganizationName(any())).thenReturn(Optional.empty());

        organizationService.modifyOrganization(1,request);

        verify(organizationRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("조직 이름 수정 실패 - 없는 조직")
    void modifyOrganizationFail1(){
        ModifyOrgsRequest request = ModifyOrgsRequest.builder()
                .organizationName("nhn 김해").build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(CustomException.class,()->organizationService.modifyOrganization(1,request));

        verify(organizationRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("조직 이름 수정 실패 - 이미 사용중인 이름")
    void modifyOrganizationFail2(){
        ModifyOrgsRequest request = ModifyOrgsRequest.builder()
                .organizationName("nhn 김해").build();

        Organization organization1 = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        Organization organization2 = Organization.builder()
                .organizationId(2)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization1));
        when(organizationRepository.findByOrganizationName(any())).thenReturn(Optional.of(organization2));

        Assertions.assertThrows(CustomException.class,()->organizationService.modifyOrganization(1,request));

        verify(organizationRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("조직 삭제")
    void deleteOrganizationSuccess(){
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));

        organizationService.deleteOrganization(1);

        verify(organizationRepository,times(1)).delete(any());
    }

    @Test
    @DisplayName("gatewaySn & controllerSn 추가")
    void putGatewaySnControllerSnSuccess(){
        PutGatewayOrControllerDto dto = PutGatewayOrControllerDto.builder()
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));
        organizationService.putSerialNumber(1, dto);

        verify(organizationRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("gatewaySn만 추가")
    void putGatewaySnSuccess(){
        PutGatewayOrControllerDto dto = PutGatewayOrControllerDto.builder()
                .gatewaySn("gatewaySn")
                .controllerSn(null)
                .build();

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));
        organizationService.putSerialNumber(1, dto);

        verify(organizationRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("controllerSn만 추가")
    void putControllerSnSuccess(){
        PutGatewayOrControllerDto dto = PutGatewayOrControllerDto.builder()
                .gatewaySn(null)
                .controllerSn("controllerSn")
                .build();

        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));
        organizationService.putSerialNumber(1, dto);

        verify(organizationRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("gatewaySn & controllerSn 추가 실패 - 없는 조직")
    void putGatewaySnControllerSnFail(){
        PutGatewayOrControllerDto dto = PutGatewayOrControllerDto.builder()
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> organizationService.putSerialNumber(1, dto));

        verify(organizationRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("gatewaySn 삭제")
    void deleteGatewaySnSuccess(){
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));
        organizationService.deleteGatewaySn(1);

        verify(organizationRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("gatewaySn 삭제 실패 - 없는 조직")
    void deleteGatewaySnFail(){
        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> organizationService.deleteGatewaySn(1));

        verify(organizationRepository,times(0)).save(any());
    }

    @Test
    @DisplayName("controllerSn 삭제")
    void deleteControllerSnSuccess(){
        Organization organization = Organization.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("gatewaySn")
                .controllerSn("controllerSn")
                .build();

        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.of(organization));
        organizationService.deleteControllerSn(1);

        verify(organizationRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("controllerSn 삭제 실패 - 없는 조직")
    void deleteControllerFail(){
        when(organizationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> organizationService.deleteGatewaySn(1));

        verify(organizationRepository,times(0)).save(any());
    }
}



