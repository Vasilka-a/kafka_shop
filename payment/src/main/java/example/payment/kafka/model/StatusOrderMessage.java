package example.payment.kafka.model;

import example.payment.entity.OrderStatus;
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
