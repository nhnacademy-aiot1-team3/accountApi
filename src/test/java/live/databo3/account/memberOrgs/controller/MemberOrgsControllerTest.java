package live.databo3.account.memberOrgs.controller;

import live.databo3.account.member.entity.Roles;
import live.databo3.account.memberOrgs.dto.GetMembersByStateResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsListResponse;
import live.databo3.account.memberOrgs.dto.GetOrgsWithoutMemberResponse;
import live.databo3.account.memberOrgs.service.MemberOrgsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberOrgsController.class)
@AutoConfigureMockMvc
public class MemberOrgsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberOrgsService memberOrgsService;

    @Test
    @DisplayName(" 성공")
    void booleanMemberOrgs() throws Exception {
        given(memberOrgsService.booleanMemberOrgs(any(), any())).willReturn(3);

        mockMvc.perform(get("/api/account/organizations/1/members/admin"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("권한 없음")
    void booleanMemberOrgsFail1() throws Exception {
        given(memberOrgsService.booleanMemberOrgs(any(), any())).willReturn(1);

        mockMvc.perform(get("/api/account/organizations/1/members/admin"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("인증되지 않음")
    void booleanMemberOrgsFail2() throws Exception {
        given(memberOrgsService.booleanMemberOrgs(any(), any())).willReturn(2);

        mockMvc.perform(get("/api/account/organizations/1/members/admin"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("member를 제외한 조직 리스트 가져오기")
    void getOrganizatioinsWithoutMember() throws Exception {
        GetOrgsWithoutMemberResponse response1 = GetOrgsWithoutMemberResponse.builder()
                .organizationId(1)
                .organizationName("nhn 김해")
                .build();
        GetOrgsWithoutMemberResponse response2 = GetOrgsWithoutMemberResponse.builder()
                .organizationId(2)
                .organizationName("nhn 대구")
                .build();
        List<GetOrgsWithoutMemberResponse> responseList = List.of(response1, response2);

        given(memberOrgsService.getOrganizationsWithoutMember(any())).willReturn(responseList);

        mockMvc.perform(get("/api/account/organizations/members-without/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizationId").value(1))
                .andExpect(jsonPath("$[1].organizationId").value(2))
                .andExpect(jsonPath("$[0].organizationName").value("nhn 김해"))
                .andExpect(jsonPath("$[1].organizationName").value("nhn 대구"))
                .andDo(print());
    }

    @Test
    @DisplayName("member가 해당하는 조직들을 가져오는 method")
    void getOrganizatioinsByMember() throws Exception {

        GetOrgsListResponse response1 = GetOrgsListResponse.builder()
                .roleName("ROLE_OWNER")
                .state(1)
                .organizationId(1)
                .organizationName("nhn 김해")
                .build();
        GetOrgsListResponse response2 = GetOrgsListResponse.builder()
                .roleName("ROLE_VIEWER")
                .state(2)
                .organizationId(2)
                .organizationName("nhn 서울")
                .build();

        List<GetOrgsListResponse> responseList = List.of(response1, response2);

        given(memberOrgsService.getOrganizations(any())).willReturn(responseList);

        mockMvc.perform(get("/api/account/organizations/members/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").exists())
                .andExpect(jsonPath("$[1].roleName").exists())
                .andExpect(jsonPath("$[0].state").value(1))
                .andExpect(jsonPath("$[1].state").value(2))
                .andExpect(jsonPath("$[0].organizationId").value(1))
                .andExpect(jsonPath("$[1].organizationId").value(2))
                .andExpect(jsonPath("$[0].organizationName").value("nhn 김해"))
                .andExpect(jsonPath("$[1].organizationName").value("nhn 서울"))
                .andDo(print());
    }

    @Test
    @DisplayName("상태와 role에 따라 멤버 들고 오기")
    void getMemberByState() throws Exception {
        GetMembersByStateResponse response1 = GetMembersByStateResponse.builder()
                .memberId("testId1")
                .memberEmail("nhn1@academy.com")
                .roleName("ROLE_OWNER")
                .state(2)
                .build();
        GetMembersByStateResponse response2 = GetMembersByStateResponse.builder()
                .memberId("testId2")
                .memberEmail("nhn2@academy.com")
                .roleName("ROLE_VIEWER")
                .state(2)
                .build();

        List<GetMembersByStateResponse> responseList = List.of(response1, response2);

        given(memberOrgsService.getMemberListByState(any(Integer.class), any(Integer.class), any(String.class))).willReturn(responseList);

        mockMvc.perform(get("/api/account/organizations/1/members/state?state=2&role=ROLE_OWNER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value("testId1"))
                .andExpect(jsonPath("$[1].memberId").value("testId2"))
                .andExpect(jsonPath("$[0].memberEmail").value("nhn1@academy.com"))
                .andExpect(jsonPath("$[1].memberEmail").value("nhn2@academy.com"))
                .andExpect(jsonPath("$[0].roleName").value("ROLE_OWNER"))
                .andExpect(jsonPath("$[1].roleName").value("ROLE_VIEWER"))
                .andExpect(jsonPath("$[0].state").value(2))
                .andExpect(jsonPath("$[1].state").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 조직 구성원에 특정 멤버가 속하도록 db 저장")
    void createMemberOrgs() throws Exception {
        doNothing().when(memberOrgsService).addMemberOrgs(any(Integer.class), any(String.class));

        mockMvc.perform(post("/api/account/organizations/1/members/testId"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("특정 조직 구성원에 트정 멤버가 속하는 요청 상태를 허용으로 변경")
    void modifyState1To2() throws Exception {
        doNothing().when(memberOrgsService).modifyState(any(Integer.class),any(String.class),any(Integer.class));
        mockMvc.perform(put("/api/account/organizations/1/members/testId?state=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("특정 조직 구성원에 속한 특정 멤버에 대한 매핑 삭제")
    void deleteMemberOrgs() throws Exception {
        doNothing().when(memberOrgsService).deleteMemberOrgs(any(Integer.class), any(String.class));

        mockMvc.perform(delete("/api/account/organizations/1/members/testId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }
}
