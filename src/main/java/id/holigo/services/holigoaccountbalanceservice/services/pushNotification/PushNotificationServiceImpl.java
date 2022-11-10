package id.holigo.services.holigoaccountbalanceservice.services.pushNotification;

import id.holigo.services.common.model.PushNotificationDto;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {

    private PushNotificationFeignClient pushNotificationFeignClient;

    @Override
    public void sendPushNotification(PushNotificationDto pushNotificationDto) {
        pushNotificationFeignClient.postPushNotification(pushNotificationDto, pushNotificationDto.getUserId(), LocaleContextHolder.getLocale().toString());
    }
}
