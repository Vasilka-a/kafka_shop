package example.shipping.kafka.model;

import example.shipping.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaidOrder {
    private Long orderNumber;
    private Long userId;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private OrderStatus orderStatus;
}

