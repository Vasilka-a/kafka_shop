package example.notification.service;

import example.notification.entity.OrderStatus;
import example.notification.kafka.model.ShippingMessage;
import example.notification.kafka.model.StatusOrderMessage;
import example.notification.kafka.producer.NotificationProducer;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void sentNotification(ShippingMessage shippingMessage) {
        if (shippingMessage.getOrderStatus().equals(OrderStatus.SHIPPED)) {
            StatusOrderMessage message = StatusOrderMessage
                    .builder()
                    .orderNumber(shippingMessage.getOrderNumber())
                    .userId(shippingMessage.getUserId())
                    .orderStatus(OrderStatus.DONE)
                    .build();
            notificationProducer.sendMessage(message);
        }
    }
}
