package live.databo3.account.notification.service;

import live.databo3.account.notification.dto.AddNotificationRequest;
import live.databo3.account.notification.dto.GetNotificationListResponse;
import live.databo3.account.notification.dto.GetNotificationResponse;
import live.databo3.account.notification.dto.ModifyNotificationRequest;

import java.util.List;

public interface NotificationService {
    GetNotificationResponse getNotification(Long notificationId);

    List<GetNotificationListResponse> getNotificiations();

    void addNotification(AddNotificationRequest response, String memberId);

    void modifyNotification(Long notificationId, ModifyNotificationRequest request, String memberId);

    void deleteNotification(Long notificationId);
}

