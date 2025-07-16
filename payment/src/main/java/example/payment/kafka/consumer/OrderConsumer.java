package example.payment.kafka.consumer;

import example.payment.kafka.model.OrderMessage;
import example.payment.kafka.producer.KafkaLogProducer;
import example.payment.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final KafkaLogProducer logProducer;
    private final PaymentService paymentService;

    public OrderConsumer(KafkaLogProducer logProducer, PaymentService paymentService) {
        this.logProducer = logProducer;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${kafka.topic.orders}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentMessage(OrderMessage orderMessage, Acknowledgment acknowledgment) {
        try {
            paymentService.processPayment(orderMessage);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logProducer.sendLogError("Payment-service", "Error in message processing");
        }
    }
}