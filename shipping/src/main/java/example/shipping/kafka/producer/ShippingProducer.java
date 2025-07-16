package example.shipping.kafka.producer;

import example.shipping.kafka.model.ShippingMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShippingProducer {
    private final KafkaTemplate<String, ShippingMessage> kafkaTemplate;
    private final String sentOrderTopic;

    public ShippingProducer(KafkaTemplate<String, ShippingMessage> kafkaTemplate,
                            @Value("${kafka.topic.sent}") String sentOrderTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.sentOrderTopic = sentOrderTopic;
    }

    public void sendMessage(ShippingMessage message) {
        kafkaTemplate.send(sentOrderTopic, message);
    }
}
