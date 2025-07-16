package example.orders.repository;

import example.orders.model.Order;
import example.orders.model.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select order from Order order where order.orderNumber = :orderNumber")
    Optional<Order> findOrderByOrderNumber(Long orderNumber);

    @Modifying
    @Query("UPDATE Order order SET order.orderStatus = :newOrderStatus where order.orderNumber = :orderNumber")
    int updateOrderStatusByOrderNumber(Long orderNumber, OrderStatus newOrderStatus);
}
