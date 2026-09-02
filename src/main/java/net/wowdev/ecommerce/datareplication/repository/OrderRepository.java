package net.wowdev.ecommerce.datareplication.repository;

import java.util.UUID;
import net.wowdev.ecommerce.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {}
