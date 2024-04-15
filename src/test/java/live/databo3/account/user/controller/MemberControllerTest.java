package live.databo3.account.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.account.user.dto.JoinResponseDto;
import live.databo3.account.user.dto.LoginInfoResponseDto;
import live.databo3.account.user.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}