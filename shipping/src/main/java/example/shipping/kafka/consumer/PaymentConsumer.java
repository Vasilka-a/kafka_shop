package example.shipping.kafka.consumer;

import example.shipping.kafka.model.PaidOrder;
import example.shipping.kafka.producer.KafkaLogProducer;
import example.shipping.service.ShippingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    private final KafkaLogProducer logProducer;
    private final ShippingService shippingService;

    public PaymentConsumer(KafkaLogProducer logProducer, ShippingService shippingService) {
        this.logProducer = logProducer;
        this.shippingService = shippingService;
    }

    @KafkaListener(topics = "${kafka.topic.paid}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleShippingMessage(PaidOrder paidOrder, Acknowledgment acknowledgment) {
        try {
            shippingService.packAndShipOrder(paidOrder);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logProducer.sendLogError("Shipping-service", "Error in message processing");
        }
    }
}