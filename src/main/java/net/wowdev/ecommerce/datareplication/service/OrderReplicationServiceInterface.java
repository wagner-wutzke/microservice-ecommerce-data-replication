package net.wowdev.ecommerce.datareplication.service;

import java.util.UUID;
import net.wowdev.ecommerce.domain.dto.OrderDTO;

public interface OrderReplicationServiceInterface {

  OrderDTO findById(UUID id);

  OrderDTO replicate(OrderDTO orderDTO);
}
