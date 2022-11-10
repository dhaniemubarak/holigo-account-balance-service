package id.holigo.services.holigoaccountbalanceservice.services.pushNotification;

import id.holigo.services.common.model.PushNotificationDto;

public interface PushNotificationService {

    void sendPushNotification(PushNotificationDto pushNotificationDto);
}
