package live.databo3.account.notification.controller;

import live.databo3.account.notification.dto.AddNotificationRequest;
import live.databo3.account.notification.dto.GetNotificationListResponse;
import live.databo3.account.notification.dto.GetNotificationResponse;
import live.databo3.account.notification.dto.ModifyNotificationRequest;
import live.databo3.account.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 공지사항 DB 관련 요청을 처리하는 Controller
 * @author jihyeon
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 특정 공지사항 정보 반환하기
     * @param notificationId 공지사항 담당 번호
     * @return GetNotificationResponse(notificationId, title, contents, date, memberId, file)
     * @since 1.0.0
     */
    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<GetNotificationResponse> getNotification(@PathVariable Long notificationId) {
        GetNotificationResponse response = notificationService.getNotification(notificationId);
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 공지사항 반환하기
     * @return GetNotificationListResponse(notificationId, title, author, date)의 리스트
     * @since 1.0.0
     */
    @GetMapping("/notifications")
    public ResponseEntity<List<GetNotificationListResponse>> getNotifications(){
        List<GetNotificationListResponse> noticeList = notificationService.getNotificiations();
        return ResponseEntity.ok(noticeList);
    }

    /**
     * 공지사항 추가
     * @param response AddNotificationRequest(title, contents, file)
     * @param memberId 멤버 아이디
     * @return ResponseEntity 200, message: success
     * @since 1.0.0
     */
    @PostMapping("/notification")
    public ResponseEntity<HashMap<String, String>> postNotifications(@RequestBody AddNotificationRequest response, @RequestHeader("X-USER-ID") String memberId) {
        notificationService.addNotification(response, memberId);
        HashMap<String, String> result = new HashMap<>();
        result.put("message", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 공지사항 수정
     * @param notificationId 공지사항 담당 번호
     * @param request 수정사항 (title, contents, file)
     * @param memberId 멤버 아이디
     * @return ResponseEntity 200, message: success
     * @since 1.0.0
     */
    @PutMapping("/notifications/{notificationId}")
    public ResponseEntity<HashMap<String, String>> putNotification(@PathVariable Long notificationId, @RequestBody ModifyNotificationRequest request, @RequestHeader("X-USER-ID") String memberId) {
        notificationService.modifyNotification(notificationId, request, memberId);
        HashMap<String, String> result = new HashMap<>();
        result.put("message", "success");
        return ResponseEntity.ok(result);
    }

    /**
     * 공지사항 삭제
     * @param notificationId 공지사항 담당 번호
     * @return ResponseEntity 200, message: success
     * @since 1.0.0
     */
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<HashMap<String, String>> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        HashMap<String, String> result = new HashMap<>();
        result.put("message", "success");
        return ResponseEntity.ok(result);
    }

}

