package example.orders.kafka.consumer;

import example.orders.kafka.model.StatusOrderMessage;
import example.orders.kafka.producer.KafkaLogProducer;
import example.orders.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class UpdateStatusOrderConsumer {

    private final OrderService orderService;
    private final KafkaLogProducer logProducer;

    public UpdateStatusOrderConsumer(OrderService orderService, KafkaLogProducer logProducer) {
        this.orderService = orderService;
        this.logProducer = logProducer;
    }

    @KafkaListener(topics = "${kafka.topic.update-status}", groupId = "${spring.kafka.consumer.group-id-update-status}")
    public void handleOrderMessage(StatusOrderMessage message, Acknowledgment acknowledgment) {
        try {
            orderService.updateOrderStatus(message);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            logProducer.sendLogError("Order-service", "Error in message processing");
        }
    }

}