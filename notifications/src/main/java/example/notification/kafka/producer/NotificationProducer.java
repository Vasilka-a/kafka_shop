package example.notification.kafka.producer;

import example.notification.kafka.model.StatusOrderMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, StatusOrderMessage> kafkaTemplate;
    private final String updateStatusTopic;

    public NotificationProducer(KafkaTemplate<String, StatusOrderMessage> kafkaTemplate,
                                @Value("${kafka.topic.update-status}") String updateStatusTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.updateStatusTopic = updateStatusTopic;
    }

    public void sendMessage(StatusOrderMessage message) {
        kafkaTemplate.send(updateStatusTopic, message);
    }
}
