package org.bahmni.module.feedintegration.repository;

import org.bahmni.module.feedintegration.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
}
