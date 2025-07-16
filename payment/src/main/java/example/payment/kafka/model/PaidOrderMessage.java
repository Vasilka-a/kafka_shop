package example.payment.kafka.model;

import example.payment.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidOrderMessage {
    private Long orderNumber;
    private Long userId;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private OrderStatus orderStatus;
}

