package example.orders.controller;

import example.orders.dto.OrderRequest;
import example.orders.kafka.producer.KafkaLogProducer;
import example.orders.model.Order;
import example.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class OrdersController {
    private final OrderService orderService;
    private final KafkaLogProducer logProducer;

    public OrdersController(OrderService orderService, KafkaLogProducer logProducer) {
        this.orderService = orderService;
        this.logProducer = logProducer;
    }

    @PostMapping("/{userId}/order")
    public ResponseEntity<String> createOrder(@PathVariable Long userId, @Valid @RequestBody OrderRequest request) {
        Order savedOrder = orderService.createNewOrder(userId, request);
        logProducer.sendLogInfo("Orders-service", String.format("New order has been created: %d", savedOrder.getOrderNumber()));
        return ResponseEntity.ok().body("The order has been created");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getNotification(@PathVariable Long userId, @RequestParam Long orderNumber) {
        String message = orderService.sentNotificationToUser(userId, orderNumber);
        logProducer.sendLogInfo("Orders-service", String.format("Send information to user: %d, %s", userId, message));
        return ResponseEntity.ok().body(message);
    }
}
