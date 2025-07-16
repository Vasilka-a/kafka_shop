package example.orders.kafka.model;

import example.orders.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusOrderMessage {
    private Long orderNumber;
    private OrderStatus orderStatus;
}
