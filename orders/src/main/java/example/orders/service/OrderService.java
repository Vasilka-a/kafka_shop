package example.orders.service;

import example.orders.dto.OrderRequest;
import example.orders.exception.DataHasNotUpdateException;
import example.orders.kafka.model.StatusOrderMessage;
import example.orders.kafka.producer.KafkaLogProducer;
import example.orders.kafka.producer.OrderProducer;
import example.orders.model.Order;
import example.orders.model.OrderStatus;
import example.orders.model.User;
import example.orders.repository.OrderRepository;
import example.orders.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;
    private final KafkaLogProducer logProducer;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, OrderProducer orderProducer, KafkaLogProducer logProducer) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
        this.logProducer = logProducer;
    }

    @Transactional
    public Order createNewOrder(Long userId, OrderRequest order) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User by id: %d not found", userId)));

        Order newOrder = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PROCESS)
                .shippingAddress(order.getShippingAddress())
                .totalPrice(order.getTotalPrice())
                .build();

        Order savedOrder = orderRepository.save(newOrder);
        orderProducer.sendMessage(newOrder);
        return savedOrder;
    }

    @Transactional
    public void updateOrderStatus(StatusOrderMessage orderMessage) {
        Long orderNumber = orderMessage.getOrderNumber();
        Order order = orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order by order number: %d not found", orderNumber)));

        if (orderRepository.updateOrderStatusByOrderNumber(order.getOrderNumber(), orderMessage.getOrderStatus()) == 0) {
            throw new DataHasNotUpdateException("The order status has not update" + orderNumber);
        }
        logProducer.sendLogInfo("Orders-service",
                String.format("The order status has been successfully updated. Order number: d%. New status: %s", orderNumber, orderMessage.getOrderStatus()));
    }

    public String sentNotificationToUser(Long userId, Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order by order number: %d not found", orderNumber)));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("The order number or user ID is incorrect");
        }

        if (order.getOrderStatus().equals(OrderStatus.DONE)) {
            return "The order is done " + orderNumber;
        }
        return "The order in process " + orderNumber;
    }

    private Long generateOrderNumber() {
        return System.currentTimeMillis();
    }
}

