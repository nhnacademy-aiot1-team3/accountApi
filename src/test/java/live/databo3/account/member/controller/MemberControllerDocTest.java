package live.databo3.account.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.member.dto.MemberDto;
import live.databo3.account.member.dto.UpdateMemberStateRequestDto;
import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.entity.States;
import live.databo3.account.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
public class MemberControllerDocTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    MockMvc mockMvc;
    private ObjectMapper objectMapper;

    List<MemberDto> memberDtoList;

    @BeforeEach
    void setup() throws Exception {
        objectMapper = new ObjectMapper();
        memberDtoList = List.of(
                MemberDto.builder()
                        .number(1L)
                        .id("test1")
                        .email("test1@email.com")
                        .role(Roles.ROLES.ROLE_ADMIN)
                        .state(States.STATES.ACTIVE)
                        .lastLoginDateTime(LocalDateTime.now())
                        .build(),
                MemberDto.builder()
                        .number(2L)
                        .id("test2")
                        .email("test2@email.com")
                        .role(Roles.ROLES.ROLE_OWNER)
                        .state(States.STATES.WAIT)
                        .lastLoginDateTime(LocalDateTime.now())
                        .build(),
                MemberDto.builder()
                        .number(3L)
                        .id("test3")
                        .email("test3@email.com")
                        .role(Roles.ROLES.ROLE_VIEWER)
                        .state(States.STATES.QUIT)
                        .lastLoginDateTime(LocalDateTime.now())
                        .build(),
                MemberDto.builder()
                        .number(4L)
                        .id("test4")
                        .email("test4@email.com")
                        .role(Roles.ROLES.ROLE_ADMIN)
                        .state(States.STATES.ACTIVE)
                        .lastLoginDateTime(LocalDateTime.now())
                        .build(),
                MemberDto.builder()
                        .number(5L)
                        .id("test5")
                        .email("test5@email.com")
                        .role(Roles.ROLES.ROLE_OWNER)
                        .state(States.STATES.WAIT)
                        .lastLoginDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Test
    @DisplayName("멤버 전체 조회 테스트")
    void getAllMembers() throws Exception {

        given(memberService.getMemberList(any(), any())).willReturn(memberDtoList);

        mockMvc.perform(get("/api/account/member/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id").value("test1"))
                .andExpect(jsonPath("$[1].id").value("test2"))
                .andExpect(jsonPath("$[2].id").value("test3"))
                .andExpect(jsonPath("$[3].id").value("test4"))
                .andExpect(jsonPath("$[4].id").value("test5"))
                .andDo(document("get-member-list-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].number").description("멤버 번호"),
                                fieldWithPath("[].id").description("멤버 ID"),
                                fieldWithPath("[].email").description("멤버 이메일"),
                                fieldWithPath("[].role").description("멤버 역할"),
                                fieldWithPath("[].state").description("멤버 상태"),
                                fieldWithPath("[].lastLoginDateTime").description("마지막 로그인 일시")
                        )));
    }

    @Test
    @DisplayName("해당하는 role, state 를 가진 전체 멤버 조회")
    void getFilteredMembers() throws Exception {
        Long roleId = 2L;
        Long stateId = 1L;
        given(memberService.getMemberList(roleId, stateId)).willReturn(
                memberDtoList.stream()
                        .filter(memberDto -> memberDto.getRole().equals(Roles.ROLES.ROLE_OWNER) && memberDto.getState().equals(States.STATES.WAIT))
                        .collect(Collectors.toList())
        );
        mockMvc.perform(get("/api/account/member/list")
                        .param("roleId", String.valueOf(roleId))
                        .param("stateId", String.valueOf(stateId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("test2"))
                .andExpect(jsonPath("$[1].id").value("test5"))
                .andDo(document("get-filter-member-list-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("roleId").description("권한 ID"),
                                parameterWithName("stateId").description("상태 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].number").description("멤버 번호"),
                                fieldWithPath("[].id").description("멤버 ID"),
                                fieldWithPath("[].email").description("멤버 이메일"),
                                fieldWithPath("[].role").description("멤버 역할"),
                                fieldWithPath("[].state").description("멤버 상태"),
                                fieldWithPath("[].lastLoginDateTime").description("마지막 로그인 일시")
                        )));
    }
    @Test
    @DisplayName("멤버 권한 변경 성공")
    void modifyMemberRoleSuccess() throws Exception {
        UpdateMemberStateRequestDto updateMemberStateRequestDto = new UpdateMemberStateRequestDto();
        ReflectionTestUtils.setField(updateMemberStateRequestDto, "memberId", "test1");
        ReflectionTestUtils.setField(updateMemberStateRequestDto, "stateId", 1L);


        mockMvc.perform(put("/api/account/member/modify/state")
                        .content(objectMapper.writeValueAsString(updateMemberStateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("put-modify-member-state-success",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberId").description("멤버 ID"),
                                fieldWithPath("stateId").description("상태 ID")
                        )
                ));
    }

    @Test
    @DisplayName("멤버 권한 변경 실패 - 멤버가 없을때")
    void modifyMemberStateFailByNoSuchMember() throws Exception {
        UpdateMemberStateRequestDto updateMemberStateRequestDto = new UpdateMemberStateRequestDto();
        ReflectionTestUtils.setField(updateMemberStateRequestDto, "memberId", "NOT_FOUND");
        ReflectionTestUtils.setField(updateMemberStateRequestDto, "stateId", 1L);

        doThrow(new CustomException(ErrorCode.MEMBER_NOT_FOUND)).when(memberService).modifyMemberState(any(), any());


        mockMvc.perform(put("/api/account/member/modify/state")
                        .content(objectMapper.writeValueAsString(updateMemberStateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.resultCode").value(404))
                .andExpect(jsonPath("$.header.resultMessage").value("조회한 멤버가 없습니다."))
                .andExpect(jsonPath("$.body.message").value("조회한 멤버가 없습니다."))
                .andDo(print())
                .andDo(document("put-modify-member-state-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("memberId").description("멤버 ID"),
                                fieldWithPath("stateId").description("상태 ID")
                        ),
                        responseFields(
                                fieldWithPath("header.resultCode").description("결과 코드"),
                                fieldWithPath("header.resultMessage").description("결과 메시지"),
                                fieldWithPath("header.localDateTime").description("요청 시간"),
                                fieldWithPath("body.message").description("실패 메시지")
                        )
                ));

    }

}
