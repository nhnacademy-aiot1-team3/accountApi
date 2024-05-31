package live.databo3.account.notification.service;

import live.databo3.account.exception.CustomException;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.entity.Roles;
import live.databo3.account.member.entity.States;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.notification.dto.AddNotificationRequest;
import live.databo3.account.notification.dto.GetNotificationListResponse;
import live.databo3.account.notification.dto.GetNotificationResponse;
import live.databo3.account.notification.dto.ModifyNotificationRequest;
import live.databo3.account.notification.entity.Notification;
import live.databo3.account.notification.repository.NotificationRepository;
import live.databo3.account.notification.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("모든 공지사항 조회")
    void getNotifications() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);
        Notification notification1 = Notification.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();
        Notification notification2 = Notification.builder()
                .notificationId(2L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification1);
        notificationList.add(notification2);

        given(notificationRepository.findAll()).willReturn(notificationList);


        List<GetNotificationListResponse> responseList = notificationService.getNotificiations();

        Assertions.assertEquals(responseList.size(), 2);
        Assertions.assertEquals(responseList.get(0).getNotificationId(), 1L);
        Assertions.assertEquals(responseList.get(1).getNotificationId(), 2L);
        Assertions.assertEquals(responseList.get(0).getTitle(), "제목");
        Assertions.assertEquals(responseList.get(1).getTitle(), "제목");
        Assertions.assertEquals(responseList.get(0).getAuthor(), "member");
        Assertions.assertEquals(responseList.get(1).getAuthor(), "member");
        Assertions.assertNotNull(responseList.get(0).getDate());
        Assertions.assertNotNull(responseList.get(1).getDate());
    }

    @Test
    @DisplayName("특정 공지사항 조회 - 성공")
    void getNotification() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);

        Notification notification = Notification.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();
        given(notificationRepository.findById(any())).willReturn(Optional.of(notification));

        GetNotificationResponse response = notificationService.getNotification(1L);

        Assertions.assertEquals(response.getNotificationId(), 1L);
        Assertions.assertEquals(response.getTitle(), "제목");
        Assertions.assertEquals(response.getContents(), "내용");
        Assertions.assertEquals(response.getMemberId(), "member");
        Assertions.assertNotNull(response.getDate());
    }

    @Test
    @DisplayName("특정 공지사항 조회 실패 - 없는 공지사항 조회")
    void getNotificationFail() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);
        Notification notification = Notification.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();
        try {
            GetNotificationResponse response = notificationService.getNotification(1L);
        } catch (Exception e) {
            Assertions.assertEquals("조회한 공지사항을 찾을 수 없습니다", e.getMessage());
            Assertions.assertEquals(CustomException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("공지사항 추가 - 성공")
    void createNotification() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);
        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));

        AddNotificationRequest request = AddNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .build();

        notificationService.addNotification(request, "member1");

        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("공지사항 추가 실패 - 없는 멤버 조회")
    void createNotificationFail() {
//        Roles roles = new Roles();
//        States states = new States();
//        Member member = Member.createMember("member", "pw", "email", roles, states);

        AddNotificationRequest request = AddNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .build();
        try{
            notificationService.addNotification(request, "member1");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals( "조회한 멤버가 없습니다.", e.getMessage());
        }
    }

    @Test
    @DisplayName("공지사항 수정")
    void modifyNotification() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);
        Notification notification = Notification.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();

        ModifyNotificationRequest request = ModifyNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .build();

        given(memberRepository.findByMemberId(any())).willReturn(Optional.of(member));
        given(notificationRepository.findById(any())).willReturn(Optional.of(notification));

        notificationService.modifyNotification(1L, request, "testId");

        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("공지사항 수정 실패 - 없는 공지사항")
    void modifyNotificationFail1() {
        ModifyNotificationRequest request = ModifyNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .build();

        try{
            notificationService.modifyNotification(1L, request, "testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 공지사항을 찾을 수 없습니다", e.getMessage());
        }

        verify(notificationRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("공지사항 수정 실패 - 없는 멤버")
    void modifyNotificationFail2() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);
        Notification notification = Notification.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();

        ModifyNotificationRequest request = ModifyNotificationRequest.builder()
                .title("제목")
                .contents("내용")
                .build();

        given(notificationRepository.findById(any())).willReturn(Optional.of(notification));

        try{
            notificationService.modifyNotification(1L, request, "testId");
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 멤버가 없습니다.", e.getMessage());
        }
        verify(notificationRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("공지사항 삭제 성공")
    void deleteNotification() {
        Roles roles = new Roles();
        States states = new States();
        Member member = Member.createMember("member", "pw", "email", roles, states);
        Notification notification = Notification.builder()
                .notificationId(1L)
                .title("제목")
                .contents("내용")
                .date(LocalDateTime.now())
                .member(member).build();

        given(notificationRepository.findById(any())).willReturn(Optional.of(notification));

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("공지사항 삭제 실패 - 없는 공지사항")
    void deleteNotificationFail() {
        try{
            notificationService.deleteNotification(1L);
        } catch (Exception e) {
            Assertions.assertEquals(CustomException.class, e.getClass());
            Assertions.assertEquals("조회한 공지사항을 찾을 수 없습니다", e.getMessage());
        }

        verify(notificationRepository, times(0)).delete(any());
    }
}
