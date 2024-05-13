package live.databo3.account.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.account.notification.dto.AddNotificationRequest;
import live.databo3.account.notification.dto.GetNotificationListResponse;
import live.databo3.account.notification.dto.GetNotificationResponse;
import live.databo3.account.notification.dto.ModifyNotificationRequest;
import live.databo3.account.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("특정 공지사항 조회 성공")
    void getNotification() throws Exception {
        GetNotificationResponse getNotificationResponse = GetNotificationResponse.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .memberId("testId1")
                .date(LocalDateTime.now())
                .file(null).build();

        given(notificationService.getNotification(any(Long.class))).willReturn(getNotificationResponse);

        mockMvc.perform(get("/api/account/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationId").value(1L))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.contents").value("내용"))
                .andExpect(jsonPath("$.memberId").value("testId1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.file").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("특정 공지사항 조회 실패")
    void getNotifications() throws Exception {
        GetNotificationListResponse listResponse1 = GetNotificationListResponse.builder()
                .notificationId(1L)
                .title("제목")
                .author("testId1")
                .date(LocalDateTime.now())
                .build();
        GetNotificationListResponse listResponse2 = GetNotificationListResponse.builder()
                .notificationId(2L)
                .title("제목")
                .author("testId2")
                .date(LocalDateTime.now())
                .build();

        List<GetNotificationListResponse> list = List.of(listResponse1,listResponse2);
        given(notificationService.getNotificiations()).willReturn(list);

        mockMvc.perform(get("/api/account/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationId").value(1L))
                .andExpect(jsonPath("$[0].title").value("제목"))
                .andExpect(jsonPath("$[0].author").value("testId1"))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[1].notificationId").value(2L))
                .andExpect(jsonPath("$[1].title").value("제목"))
                .andExpect(jsonPath("$[1].author").value("testId2"))
                .andExpect(jsonPath("$[1].date").exists())
                .andDo(print());
    }
    @Test
    void createNotifications() throws Exception {
        AddNotificationRequest request = AddNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .file(null)
                .build();

        String content = new ObjectMapper().writeValueAsString(request);

        doNothing().when(notificationService).addNotification(any(),any(String.class));
        mockMvc.perform(post("/api/account/notifications")
                        .header("X-USER-ID", "testId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    void putNotification() throws Exception {
        ModifyNotificationRequest request = ModifyNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .file(null)
                .build();

        String content = new ObjectMapper().writeValueAsString(request);

        doNothing().when(notificationService).modifyNotification(any(Long.class), any(), any(String.class));

        mockMvc.perform(put("/api/account/notifications/1")
                .header("X-USER-ID", "testId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }

    @Test
    void deleteNotification() throws Exception {
        doNothing().when(notificationService).deleteNotification(any());

        mockMvc.perform(delete("/api/account/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }
}