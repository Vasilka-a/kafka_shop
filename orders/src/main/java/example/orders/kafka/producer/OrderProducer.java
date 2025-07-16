package example.orders.kafka.producer;

import example.orders.kafka.model.OrderMessage;
import example.orders.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;
    private final String orderTopic;

    public OrderProducer(KafkaTemplate<String, OrderMessage> kafkaTemplate,
                         @Value("${kafka.topic.orders}") String orderTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderTopic = orderTopic;
    }

    public void sendMessage(Order newOrder) {
        OrderMessage orderMessage = OrderMessage
                .builder()
                .orderNumber(newOrder.getOrderNumber())
                .userId(newOrder.getOrderId())
                .orderDate(newOrder.getOrderDate())
                .orderStatus(newOrder.getOrderStatus())
                .shippingAddress(newOrder.getShippingAddress())
                .totalPrice(newOrder.getTotalPrice())
                .build();
        kafkaTemplate.send(orderTopic, orderMessage);
    }
}
