package codelab.jb9dev.order_service.repository;

import codelab.jb9dev.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
