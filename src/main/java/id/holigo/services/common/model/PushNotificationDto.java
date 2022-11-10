package id.holigo.services.common.model;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PushNotificationDto {

    private UUID id;

    private String icon;

    private Long userId;

    private PushNotificationCategoryEnum category;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp readAt;

    private String titleIndex;

    private String descriptionIndex;

    private String titleValue;

    private String descriptionValue;

    private String title;

    private String description;

    private JsonNode data;

    private String token;

    private String topic;

    private String imageUrl;


}
