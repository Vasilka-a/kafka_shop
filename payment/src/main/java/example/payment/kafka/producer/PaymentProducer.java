package example.payment.kafka.producer;

import example.payment.kafka.model.PaidOrderMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {
    private final KafkaTemplate<String, PaidOrderMessage> kafkaTemplate;
    private final String paidOrderTopic;

    public PaymentProducer(KafkaTemplate<String, PaidOrderMessage> kafkaTemplate,
                           @Value("${kafka.topic.paid}") String paidOrderTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.paidOrderTopic = paidOrderTopic;
    }

    public void sendMessage(PaidOrderMessage message) {
        kafkaTemplate.send(paidOrderTopic, message);
    }
}
