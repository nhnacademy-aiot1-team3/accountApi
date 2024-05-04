package live.databo3.account.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.account.organization.dto.GetOrgsResponse;
import live.databo3.account.organization.dto.ModifyOrgsRequest;
import live.databo3.account.organization.dto.OrgsRequest;
import live.databo3.account.organization.service.OrganizationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrganizationController.class)
@AutoConfigureMockMvc
class OrganizationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    @Test
    @DisplayName("모든 조직 조회")
    void getAllOrgs() throws Exception {
        GetOrgsResponse orgs1= GetOrgsResponse.builder()
                        .organizationId(1)
                        .organizationName("nhn 김해")
                        .gatewaySn("kjkp2jk3")
                        .controllerSn("kjlkj2kj3")
                        .build();
        GetOrgsResponse orgs2= GetOrgsResponse.builder()
                .organizationId(2)
                .organizationName("nhn 대전")
                .gatewaySn("dfkhj3hr")
                .controllerSn("kjli23")
                .build();
        List<GetOrgsResponse> orgsResponseList = List.of(orgs1,orgs2);

        given(organizationService.getAllOrganizations()).willReturn(orgsResponseList);


        mockMvc.perform(get("/api/account/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizationId").value(1))
                .andExpect(jsonPath("$[0].organizationName").value("nhn 김해"))
                .andExpect(jsonPath("$[0].gatewaySn").value("kjkp2jk3"))
                .andExpect(jsonPath("$[0].controllerSn").value("kjlkj2kj3"))
                .andExpect(jsonPath("$[1].organizationId").value(2))
                .andExpect(jsonPath("$[1].organizationName").value("nhn 대전"))
                .andExpect(jsonPath("$[1].gatewaySn").value("dfkhj3hr"))
                .andExpect(jsonPath("$[1].controllerSn").value("kjli23"))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 조직 조회")
    void getOrgs() throws Exception {
        GetOrgsResponse orgs1= GetOrgsResponse.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .gatewaySn("kjkp2jk3")
                .controllerSn("kjlkj2kj3")
                .build();

        given(organizationService.getOrganization(any())).willReturn(orgs1);

        mockMvc.perform(get("/api/account/organizations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value(1))
                .andExpect(jsonPath("$.organizationName").value("nhn 김해"))
                .andDo(print());
    }

    @Test
    @DisplayName("조직 추가")
    void createOrg() throws Exception {
        OrgsRequest orgsRequest = OrgsRequest.builder()
                .organizationName("nhn 김해")
                .gatewaySn("dfjkl123lkj")
                .controllerSn("kljekj2")
                .build();
        String content = new ObjectMapper().writeValueAsString(orgsRequest);

        doNothing().when(organizationService).addOrganization(orgsRequest);

        mockMvc.perform(post("/api/account/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @DisplayName("조직 수정")
    void updateOrg() throws Exception {
        ModifyOrgsRequest modifyOrgsRequest = ModifyOrgsRequest.builder()
                .organizationName("nhn 김해")
                .build();
        String content = new ObjectMapper().writeValueAsString(modifyOrgsRequest);

        doNothing().when(organizationService).modifyOrganization(any(Integer.class), any());

        mockMvc.perform(put("/api/account/organizations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    @DisplayName("조직 삭제")
    void deleteOrg() throws Exception {
        doNothing().when(organizationService).deleteOrganization(any());

        mockMvc.perform(delete("/api/account/organizations/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }


}
