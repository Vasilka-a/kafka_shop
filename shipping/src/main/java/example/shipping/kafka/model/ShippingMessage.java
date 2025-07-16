package example.shipping.kafka.model;

import example.shipping.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingMessage {
    private Long orderNumber;
    private Long userId;
    private OrderStatus orderStatus;
}
