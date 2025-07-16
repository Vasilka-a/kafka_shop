package example.shipping.service;

import example.shipping.entity.OrderStatus;
import example.shipping.kafka.model.PaidOrder;
import example.shipping.kafka.model.ShippingMessage;
import example.shipping.kafka.model.StatusOrderMessage;
import example.shipping.kafka.producer.KafkaLogProducer;
import example.shipping.kafka.producer.ShippingProducer;
import example.shipping.kafka.producer.UpdateStatusOrderProducer;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {
    private final KafkaLogProducer logProducer;
    private final ShippingProducer shippingProducer;
    private final UpdateStatusOrderProducer updateStatusOrderProducer;

    public ShippingService(KafkaLogProducer logProducer, ShippingProducer shippingProducer, UpdateStatusOrderProducer updateStatusOrderProducer) {
        this.logProducer = logProducer;
        this.shippingProducer = shippingProducer;
        this.updateStatusOrderProducer = updateStatusOrderProducer;
    }

    public void packAndShipOrder(PaidOrder paidOrder) {
        try {
            logProducer.sendLogInfo("Shipping-service", "The order number: " + paidOrder.getOrderNumber() + " is packed and shipped");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logProducer.sendLogInfo("Shipping-service", "The order number: " + paidOrder.getOrderNumber() + " has been sent");

        ShippingMessage message = ShippingMessage
                .builder()
                .orderNumber(paidOrder.getOrderNumber())
                .userId(paidOrder.getUserId())
                .orderStatus(OrderStatus.SHIPPED)
                .build();
        shippingProducer.sendMessage(message);

        sendStatusOrderMessage(message);
    }

    private void sendStatusOrderMessage(ShippingMessage message) {
        StatusOrderMessage statusOrderMessage = StatusOrderMessage.builder()
                .orderNumber(message.getOrderNumber())
                .orderStatus(message.getOrderStatus())
                .build();
        updateStatusOrderProducer.sendMessage(statusOrderMessage);
    }
}
