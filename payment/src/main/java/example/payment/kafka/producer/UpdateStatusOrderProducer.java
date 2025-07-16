package example.payment.kafka.producer;

import example.payment.kafka.model.StatusOrderMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UpdateStatusOrderProducer {
    private final KafkaTemplate<String, StatusOrderMessage> kafkaTemplate;
    private final String updateStatusTopic;

    public UpdateStatusOrderProducer(KafkaTemplate<String, StatusOrderMessage> kafkaTemplate,
                                     @Value("${kafka.topic.update-status}") String updateStatusTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.updateStatusTopic = updateStatusTopic;
    }

    public void sendMessage(StatusOrderMessage message) {
        kafkaTemplate.send(updateStatusTopic, message);
    }
}
