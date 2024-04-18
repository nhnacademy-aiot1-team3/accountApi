package live.databo3.account.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.account.member.dto.JoinResponseDto;
import live.databo3.account.member.dto.LoginInfoResponseDto;
import live.databo3.account.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Test
    void getMember() throws Exception {

        String testId = "testId";
        String testPw = "testPw";
        LoginInfoResponseDto response = new LoginInfoResponseDto(1L, testId, testPw, null, null, null,null);

        given(memberService.getMemberIdAndPassword(any())).willReturn(response);

        mockMvc.perform(get("/api/account/member/{memberId}", testId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(testId))
                .andExpect(jsonPath("$.memberPassword").value(testPw));
    }

    @Test
    void createMember() throws Exception {
        String testId = "memeber1";
        String testPw = "123456";
        String testEmail = "test@test.live";

        JoinResponseDto response = new JoinResponseDto(testId, testPw, testEmail);

        given(memberService.registerMember(any())).willReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/account/member/register").content(objectMapper.writeValueAsString(response)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testId))
                .andExpect(jsonPath("$.password").value(testPw))
                .andExpect(jsonPath("$.email").value(testEmail));

    }

    @Test
    void changeState() throws Exception {
        mockMvc.perform(put("/api/account/member/state").header("X-USER-ID", anyString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    void upgradeMember() throws Exception {
        mockMvc.perform(put("/api/account/member/upgrade").header("X-USER-ID","testID").param("roleId","2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    void deleteMember() throws Exception {
        mockMvc.perform(delete("/api/account/member/delete").header("X-USER-ID", "testId"))
                .andExpect(status().isNoContent());

    }

}