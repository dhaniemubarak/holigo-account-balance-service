package id.holigo.services.holigoaccountbalanceservice.services.pushNotification;

import id.holigo.services.common.model.PushNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "holigo-push-notification-service")
public interface PushNotificationFeignClient {
    String PUSH_NOTIFICATION_PATH = "/api/v1/pushNotifications";

    @RequestMapping(method = RequestMethod.GET, value = PUSH_NOTIFICATION_PATH)
    ResponseEntity<HttpStatus> postPushNotification(@RequestBody PushNotificationDto pushNotificationDto,
                                                    @RequestHeader("user-id") Long userId,
                                                    @RequestHeader("Accept-Language") String locale);
}
