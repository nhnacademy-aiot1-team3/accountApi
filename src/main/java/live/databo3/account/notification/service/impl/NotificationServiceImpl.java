package live.databo3.account.notification.service.impl;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.member.entity.Member;
import live.databo3.account.member.repository.MemberRepository;
import live.databo3.account.notification.dto.AddNotificationRequest;
import live.databo3.account.notification.dto.GetNotificationListResponse;
import live.databo3.account.notification.dto.GetNotificationResponse;
import live.databo3.account.notification.dto.ModifyNotificationRequest;
import live.databo3.account.notification.entity.Notification;
import live.databo3.account.notification.repository.NotificationRepository;
import live.databo3.account.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService 구현체
 * @author jihyeon
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    /**
     * 공지사항 담당 번호로 공지사항 DB에서 찾아온 특정 공지사항 내용물 반환
     * @param notificationId 공지사항 담당 번호
     * @return GetNotificationResponse(notificationId, title, contents, date, memberId, file)
     * @since 1.0.0
     */
    @Override
    public GetNotificationResponse getNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        return GetNotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .contents(notification.getContents())
                .date(notification.getDate())
                .memberId(notification.getMember().getMemberId())
                .file(notification.getFile())
                .build();
    }

    /**
     * 모든 공지사항 리스트 반환
     * @return GetNotificationListResponse(notificationId, title, author, date)
     * @since 1.0.0
     */
    @Override
    public List<GetNotificationListResponse> getNotificiations() {
        List<Notification> notificationList = notificationRepository.findAll();
        List<GetNotificationListResponse> listResponses = new ArrayList<>();

        for (Notification notification : notificationList) {
            GetNotificationListResponse notificationResponse = GetNotificationListResponse.builder()
                    .notificationId(notification.getNotificationId())
                    .title(notification.getTitle())
                    .author(notification.getMember().getMemberId())
                    .date(notification.getDate())
                    .build();
            listResponses.add(notificationResponse);
        }
        return listResponses;
    }

    /**
     * 공지사항 담당번호를 기반으로 공지사항 추가
     * @param response title, contents, file
     * @param memberId 멤버 아이디
     * @since 1.0.0
     */
    @Override
    public void addNotification(AddNotificationRequest response, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        log.info("localdatetime:{}", LocalDateTime.now());
        Notification notification = Notification.builder()
                .title(response.getTitle())
                .contents(response.getContents())
                .date(LocalDateTime.now())
                .member(member)
                .file(response.getFile())
                .build();

        notificationRepository.save(notification);
    }

    /**
     * 공지사항 담당번호를 기반으로 공지사항 수정할 시 title, contents, file, memberId가 변경된다
     * @param notificationId 공지사항 담당 번호
     * @param request title, contents, file
     * @param memberId 멤버 아이디
     * @since 1.0.0
     */
    @Override
    public void modifyNotification(Long notificationId, ModifyNotificationRequest request, String memberId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        notification.change(request.getTitle(), request.getContents(), member, request.getFile());

        notificationRepository.save(notification);
    }

    /**
     * 공지사항 담당번호를 기반으로 공지사항 삭제
     * @param notificationId 공지사항 담당 번호
     * @since 1.0.0
     */
    @Override
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notificationRepository.delete(notification);
    }


}

