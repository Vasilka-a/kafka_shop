package example.payment.service;

import example.payment.entity.OrderStatus;
import example.payment.kafka.model.OrderMessage;
import example.payment.kafka.model.PaidOrderMessage;
import example.payment.kafka.model.StatusOrderMessage;
import example.payment.kafka.producer.KafkaLogProducer;
import example.payment.kafka.producer.PaymentProducer;
import example.payment.kafka.producer.UpdateStatusOrderProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final UpdateStatusOrderProducer updateStatusOrderProducer;
    private final PaymentProducer paymentProducer;
    private final KafkaLogProducer logProducer;

    public PaymentService(UpdateStatusOrderProducer updateStatusOrderProducer, PaymentProducer paymentProducer, KafkaLogProducer logProducer) {
        this.updateStatusOrderProducer = updateStatusOrderProducer;
        this.paymentProducer = paymentProducer;
        this.logProducer = logProducer;
    }

    public void processPayment(OrderMessage orderMessage) {
        try {
            logProducer.sendLogInfo("Payment-service", "The order number: " + orderMessage.getOrderNumber() + " payment process is underway");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logProducer.sendLogInfo("Payment-service", "The order number: " + orderMessage.getOrderNumber() + " is paid ");
        PaidOrderMessage paidOrderMessage = PaidOrderMessage.builder()
                .orderNumber(orderMessage.getOrderNumber())
                .userId(orderMessage.getUserId())
                .orderDate(LocalDateTime.now())
                .shippingAddress(orderMessage.getShippingAddress())
                .orderStatus(OrderStatus.PAYED)
                .build();
        paymentProducer.sendMessage(paidOrderMessage);

        sendStatusOrderMessage(paidOrderMessage);
    }

    private void sendStatusOrderMessage(PaidOrderMessage paidOrderMessage) {
        StatusOrderMessage statusOrderMessage = StatusOrderMessage.builder()
                .orderNumber(paidOrderMessage.getOrderNumber())
                .orderStatus(paidOrderMessage.getOrderStatus())
                .build();
        updateStatusOrderProducer.sendMessage(statusOrderMessage);
    }
}
