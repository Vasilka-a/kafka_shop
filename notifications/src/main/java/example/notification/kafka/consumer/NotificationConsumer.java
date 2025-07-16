package example.notification.kafka.consumer;

import example.notification.kafka.model.ShippingMessage;
import example.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${kafka.topic.sent}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleNotificationMessage(ShippingMessage shippingMessage) {
        notificationService.sentNotification(shippingMessage);
    }
}