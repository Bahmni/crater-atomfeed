package org.bahmni.module.feedintegration.repository;

import org.bahmni.module.feedintegration.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderUuid(String orderUuid);
}
